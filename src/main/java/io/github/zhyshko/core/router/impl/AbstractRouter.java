package io.github.zhyshko.core.router.impl;

import io.github.zhyshko.core.configuration.ConfigProperties;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.router.UpdateRouter;
import io.github.zhyshko.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRouter implements UpdateRouter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRouter.class);

    private ConfigProperties config;
    private Map<Integer, Route> routeHashCodes;
    private Map<Route, Map<UpdateType, Map<String, Method>>> methodMappings;

    @Override
    public Optional<ResponseEntity> handle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) throws Exception {
        beforeHandle(wrapper, i18NLabelsWrapper);
        Route routeToHandle = getRouteToHandle(wrapper);
        Optional<ResponseEntity> responseEntityOptional;
        try {
            responseEntityOptional = handleRoute(wrapper, i18NLabelsWrapper, routeToHandle);
        } catch (Exception e) {
            responseEntityOptional = handleException(wrapper, i18NLabelsWrapper, routeToHandle, e);
        }
        afterHandle(responseEntityOptional, wrapper, i18NLabelsWrapper);
        return responseEntityOptional;
    }

    @Override
    public void beforeHandle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
        // no default implementation
    }

    @Override
    public void afterHandle(Optional<ResponseEntity> responseEntityOptional,
                            UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
        // no default implementation
    }

    @Override
    public Route getRouteToHandle(UpdateWrapper wrapper) {
        return Optional.ofNullable(routeHashCodes.get(wrapper.getState()))
                .orElseThrow(() -> new IllegalArgumentException("Route for given path: " + wrapper.getState() + " not found"));
    }

    protected Optional<ResponseEntity> handleRoute(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper,
                                                   Route routeToHandle) throws Exception {
        Method methodToHandle = MethodUtil.getMethodForRequest(routeToHandle,
                RequestWrapper.builder().mapping(wrapper.getMapping()).updateType(getType()).build(),
                this.methodMappings);

        LOG.info("Routing {} to {}:{}",
                wrapper.getUserId(), routeToHandle.getClass().getSimpleName(), methodToHandle.getName());

        try {
            Object responseObj = null;
            Class<?>[] types = methodToHandle.getParameterTypes();
            if (types.length == 0) {
                responseObj = methodToHandle.invoke(routeToHandle);
            } else if (types.length == 1 && types[0] == UpdateWrapper.class) {
                responseObj = methodToHandle.invoke(routeToHandle, wrapper);
            } else if (types.length == 2 && types[0] == UpdateWrapper.class && types[1] == I18NLabelsWrapper.class) {
                if (!config.isI18nEnabled()) {
                    throw new IllegalArgumentException(
                            String.format("I18N is not enabled, but %s:%s requires i18nLabelsWrapper",
                                    routeToHandle.getClass().getSimpleName(), methodToHandle.getName()));
                }
                responseObj = methodToHandle.invoke(routeToHandle, wrapper, i18NLabelsWrapper);
            } else {
                LOG.warn("Target {}:{} does not correspond to any allowed signature, skipping",
                        routeToHandle.getClass().getSimpleName(), methodToHandle.getName());
            }

            if (responseObj instanceof ResponseEntity responseEntity) {
                return Optional.of(responseEntity);
            }
            LOG.warn("Method {}:{} returned unexpected return type {}",
                    routeToHandle.getClass().getSimpleName(), methodToHandle.getName(), responseObj);
            return Optional.empty();
        } catch (Exception e) {
            throw e;
        }

    }

    protected Optional<ResponseEntity> handleException(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper,
                                                       Route routeToHandle, Exception exception) throws Exception {
        if (exception instanceof InvocationTargetException) {
            exception = (Exception) exception.getCause();
        }

        Method exceptionHandlerMethod = MethodUtil.getExceptionHandlerMethod(routeToHandle, exception,
                this.methodMappings);

        if (exceptionHandlerMethod == null) {
            LOG.error("Unhandled exception occurred during {}", routeToHandle.getClass().getSimpleName(), exception);
            throw exception;
        }

        LOG.info("Handling exception during {}", routeToHandle.getClass().getSimpleName(), exception);

        try {
            Object responseObj = null;
            Class<?>[] types = exceptionHandlerMethod.getParameterTypes();
            if (types.length == 1) {
                responseObj = exceptionHandlerMethod.invoke(routeToHandle, exception);
            } else if (types.length == 2 && types[0] == Exception.class && types[1] == UpdateWrapper.class) {
                responseObj = exceptionHandlerMethod.invoke(routeToHandle, exception, wrapper);
            } else if (types.length == 3 && types[0] == Exception.class && types[1] == UpdateWrapper.class
                    && types[2] == I18NLabelsWrapper.class) {
                responseObj = exceptionHandlerMethod.invoke(routeToHandle, exception, wrapper, i18NLabelsWrapper);
            } else {
                LOG.warn("Exception handler {}:{} does not correspond to any allowed signature, skipping",
                        routeToHandle.getClass().getSimpleName(), exceptionHandlerMethod.getName());
            }

            if (responseObj instanceof ResponseEntity responseEntity) {
                return Optional.of(responseEntity);
            }
            LOG.warn("Exception handler {}:{} returned unexpected return type {}",
                    routeToHandle.getClass().getSimpleName(), exceptionHandlerMethod.getName(), responseObj);
            return Optional.empty();
        } catch (Exception e) {
            LOG.error("Error occurred while invoking the exception handler {}:{}",
                    routeToHandle.getClass().getSimpleName(), exceptionHandlerMethod.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setConfig(ConfigProperties config) {
        this.config = config;
    }

    @Autowired
    public void setRouteHashCodes(Map<Integer, Route> routeHashCodes) {
        this.routeHashCodes = routeHashCodes;
    }

    @Autowired
    public void setMethodMappings(
            Map<Route, Map<UpdateType, Map<String, Method>>> methodMappings) {
        this.methodMappings = methodMappings;
    }
}
