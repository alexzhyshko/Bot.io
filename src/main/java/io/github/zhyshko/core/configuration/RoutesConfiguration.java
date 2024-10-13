package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.annotation.*;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateType;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoutesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RoutesConfiguration.class);

    private static final Map<UpdateType, Class> ANNOTATION_TYPE_MAPPINGS = Map.of(
            UpdateType.MESSAGE, MessageMapping.class,
            UpdateType.CALLBACK, CallbackMapping.class,
            UpdateType.EDIT_MESSAGE, EditMessageMapping.class,
            UpdateType.DOCUMENT, DocumentMapping.class,
            UpdateType.PHOTO, PhotoMapping.class,
            UpdateType.VIDEO, VideoMapping.class,
            UpdateType.EXCEPTION, ExceptionHandler.class
    );

    private ApplicationContext applicationContext;

    @Bean
    public Map<Integer, Route> routeHashCodes(List<Route> routes) {
        Map<Integer, Route> routeHashCodes = routes.stream()
                .distinct()
                .collect(Collectors.toMap(r -> r.getClass().hashCode(), Function.identity()));

        LOG.info("Initialized route hash codes for {} routes", routeHashCodes.size());

        return routeHashCodes;
    }

    @Bean
    public Map<Route, Map<UpdateType, Map<String, Method>>> methodMappings(List<Route> routes) {
        Map<Route, Map<UpdateType, Map<String, Method>>> methodMappings = routes
                .stream()
                .distinct()
                .flatMap(r -> Arrays.stream(r.getClass().getMethods()).map(m -> Pair.of(r, m)))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> preparePerRouteMappings(e.getValue())));

        LOG.info("Initialized method mappings for {} routes", methodMappings.size());

        return methodMappings;
    }

    private Map<UpdateType, Map<String, Method>> preparePerRouteMappings(List<Method> methods) {
        return methods.stream()
                .map(m -> Pair.of(determineMethodType(m), m))
                .filter(p -> p.getKey().isPresent())
                .map(p -> Pair.of(p.getKey().get(), p.getValue()))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> preparePerMethodMappings(e.getKey(), e.getValue())));
    }

    private Map<String, Method> preparePerMethodMappings(UpdateType mappingType, List<Method> methods) {
        return methods.stream()
                .collect(Collectors.toMap(m -> getMethodPayload(mappingType, m), Function.identity()));

    }

    private Optional<UpdateType> determineMethodType(Method m) {
        return ANNOTATION_TYPE_MAPPINGS.entrySet()
                .stream()
                .filter(e -> m.isAnnotationPresent(e.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private String getMethodPayload(UpdateType mappingType, Method method) {
        boolean isAnnotationValuedInitializer;
        try {
            ANNOTATION_TYPE_MAPPINGS.get(mappingType).getMethod("value");
            isAnnotationValuedInitializer = true;
        } catch (NoSuchMethodException nsme) {
            isAnnotationValuedInitializer = false;
        }

        final boolean isAnnotationValued = isAnnotationValuedInitializer;

        return Optional.of(method)
                .map(m -> {
                    try {
                        if (isAnnotationValued) {
                            Method annotationValueMethod = ANNOTATION_TYPE_MAPPINGS.get(mappingType).getMethod("value");
                            return annotationValueMethod
                                    .invoke(m.getAnnotation(ANNOTATION_TYPE_MAPPINGS.get(mappingType))).toString();
                        } else {
                            return "*";
                        }
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Mapping could not be determined for " + method.getName()));
    }

    @Bean("entryPoint")
    @Primary
    public Route getEntryPoint() {
        Route entryPoint = applicationContext.getBeansWithAnnotation(Entrypoint.class)
                .values()
                .stream()
                .map(o -> (Route) o)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Please, annotate your entrypoint route with @Entrypoint"));

        LOG.info("Found entrypoint class {}", entryPoint.getClass().getSimpleName());

        return entryPoint;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
