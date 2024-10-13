package io.github.zhyshko.core.util;

import io.github.zhyshko.core.annotation.*;
import io.github.zhyshko.core.router.Route;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodUtil {

    public static Method getMethodForRequest(Route routeToHandle, RequestWrapper request) {
        return switch (request.getUpdateType()) {
            case MESSAGE -> getMethodForMessage(routeToHandle, (String) request.getPayload());
            case CALLBACK -> getMethodForCallback(routeToHandle, (String) request.getPayload());
            case EDIT_MESSAGE -> getMethodForEditMessage(routeToHandle, (String) request.getPayload());
            case DOCUMENT -> getMethodForDocument(routeToHandle, (DocumentPayload) request.getPayload());
            case PHOTO -> getMethodForPhoto(routeToHandle, (DocumentPayload) request.getPayload());
            case VIDEO -> getMethodForVideo(routeToHandle, (DocumentPayload) request.getPayload());
        };
    }

    public static Method getExceptionHandlerMethod(Route route, Exception exception) {
        if (exception == null) {
            return null;
        }

        Map<Class<? extends Exception>, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(ExceptionHandler.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(ExceptionHandler.class).value(), Function.identity()));

        return methodMappings.get(exception.getClass());
    }

    private static Method getMethodForMessage(Route route, String message) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(MessageMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(MessageMapping.class).value(), Function.identity()));

        return getMethod(route, methodMappings, message);
    }

    private static Method getMethodForCallback(Route route, String callbackData) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(CallbackMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(CallbackMapping.class).value(), Function.identity()));

        return getMethod(route, methodMappings, callbackData);
    }

    private static Method getMethodForDocument(Route route, DocumentPayload payload) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(DocumentMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(DocumentMapping.class).value(), Function.identity()));

        String documentFormat = ((Document) payload.getPayload()).getMimeType();

        return getMethod(route, methodMappings, documentFormat);
    }

    private static Method getMethodForPhoto(Route route, DocumentPayload payload) {
        List<Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(PhotoMapping.class))
                .toList();

        if(methodMappings.size() > 1) {
            throw new IllegalArgumentException("More than one PhotoMapping annotation on single route is not allowed currently");
        }

        return getMethod(route, methodMappings, "photo");
    }

    private static Method getMethodForVideo(Route route, DocumentPayload payload) {
        List<Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(VideoMapping.class))
                .toList();

        if(methodMappings.size() > 1) {
            throw new IllegalArgumentException("More than one VideoMapping annotation on single route is not allowed currently");
        }

        return getMethod(route, methodMappings, "video");
    }

    private static Method getMethodForEditMessage(Route route, String editedMessage) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(EditMessageMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(EditMessageMapping.class).value(), Function.identity()));

        return getMethod(route, methodMappings, editedMessage);
    }

    private static Method getMethod(Route route, Map<String, Method> methodMappings, Object payload) {
        Method defaultMethod = methodMappings.get("*");
        Method methodToHandle = methodMappings.get(payload);

        if (methodToHandle == null && defaultMethod == null) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: " + route.getClass().getSimpleName() + " and payload: " + payload);
        }

        if (methodToHandle == null) {
            methodToHandle = defaultMethod;
        }

        return methodToHandle;
    }

    private static Method getMethod(Route route, List<Method> methodMappings, Object payload) {
        if (methodMappings.isEmpty()) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: " + route.getClass().getSimpleName() + " and payload: " + payload);
        }

        return  methodMappings.get(0);
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
