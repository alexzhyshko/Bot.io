package io.github.zhyshko.core.util;

import io.github.zhyshko.core.annotation.*;
import io.github.zhyshko.core.router.Route;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodUtil {

    public static Method getMethodForMessage(Route route, String message) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(MessageMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(MessageMapping.class).value(), Function.identity()));

        Method defaultMethod = methodMappings.get("*");
        Method methodToHandle = methodMappings.get(message);

        if (methodToHandle == null && defaultMethod == null) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: " + route + " and message: " + message);
        }

        if (methodToHandle == null) {
            methodToHandle = defaultMethod;
        }

        return methodToHandle;
    }

    public static Method getMethodForCallback(Route route, String callbackData) {
        Map<String, Method> methodMappings = Stream.of(route.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(CallbackMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(CallbackMapping.class).value(), Function.identity()));

        Method defaultMethod = methodMappings.get("*");
        Method methodToHandle = methodMappings.get(callbackData);

        if (methodToHandle == null && defaultMethod == null) {
            throw new IllegalArgumentException("There is no suitable method mapping for route: " + route.getClass().getSimpleName() + " and message: " + callbackData);
        }

        if (methodToHandle == null) {
            methodToHandle = defaultMethod;
        }

        return methodToHandle;
    }
}
