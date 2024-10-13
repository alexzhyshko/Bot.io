package io.github.zhyshko.core.util;

import io.github.zhyshko.core.annotation.*;
import io.github.zhyshko.core.router.Route;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class MethodUtil {

    public static Method getMethodForRequest(Route routeToHandle, RequestWrapper request, Map<Route,
            Map<UpdateType, Map<String, Method>>> methodMappings) {
        return getMethod(routeToHandle, request, methodMappings);
    }

    public static Method getExceptionHandlerMethod(Route route, Exception exception, Map<Route,
            Map<UpdateType, Map<String, Method>>> methodMappings) {
        if (exception == null) {
            return null;
        }

        String mapping = exception.getClass().toString();

        return getMethod(route,
                RequestWrapper.builder().mapping(mapping).updateType(UpdateType.EXCEPTION).build(),
                methodMappings);
    }

    private static Method getMethod(Route routeToHandle, RequestWrapper request, Map<Route,
            Map<UpdateType, Map<String, Method>>> methodMappings) {

        UpdateType currentMethodMappingType = UpdateType.valueOf(request.getUpdateType().toString());

        String mapping = request.getMapping();

        Map<String, Method> mappings = Optional.ofNullable(methodMappings.get(routeToHandle))
                .map(m -> m.get(currentMethodMappingType))
                .orElseThrow(() -> new IllegalArgumentException("There is no suitable method mapping for route: "
                        + routeToHandle.getClass().getSimpleName() + " and mapping: " + mapping));

        if (mappings == null || mappings.isEmpty()) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: "
                    + routeToHandle.getClass().getSimpleName() + " and mapping: " + mapping);
        }

        Method targetMethod = mappings.get(mapping);
        Method defaultMethod = mappings.get("*");

        if (targetMethod == null && defaultMethod != null) {
            targetMethod = defaultMethod;
        }

        if (targetMethod == null) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: "
                    + routeToHandle.getClass().getSimpleName() + " and mapping: " + mapping);
        }

        return targetMethod;
    }

    public static Optional<Method> getViewInitializerMethod(Route route) {
        List<Method> methods = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(ViewInitializer.class))
                .toList();
        if (methods.size() > 1) {
            throw new IllegalArgumentException("More than one view initializer defined for " + route.getClass().getSimpleName());
        }
        if (methods.size() == 1) {
            return Optional.of(methods.get(0));
        }
        return Optional.empty();
    }
}
