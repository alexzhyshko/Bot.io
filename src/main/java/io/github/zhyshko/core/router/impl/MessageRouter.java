package io.github.zhyshko.core.router.impl;

import io.github.zhyshko.core.constants.SessionConstants;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.router.UpdateRouter;
import io.github.zhyshko.core.util.MethodUtil;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class MessageRouter implements UpdateRouter {

    private static final Logger LOG = LoggerFactory.getLogger(MessageRouter.class);

    private List<Route> messageRoutes;
    private Route entryPoint;

    @Override
    public Optional<ResponseEntity> handle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
        var state = wrapper.getState();
        Route routeToHandle;
        if (state == null) {
            routeToHandle = entryPoint;
            this.prepareSessionLocale(wrapper);
        } else {
            routeToHandle = messageRoutes.stream()
                    .filter(r -> r.getClass().hashCode() == state)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Route for given path: " + state + " not found"));
        }

        var message = wrapper.getUpdate().getMessage().getText();

        Method methodToHandle = MethodUtil.getMethodForMessage(routeToHandle, message);

        LOG.debug("Routing {} to {}:{}",
                wrapper.getUserId(), routeToHandle.getClass().getSimpleName(), methodToHandle.getName());

        try {
            Object responseObj = null;
            Class<?>[] types = methodToHandle.getParameterTypes();

            if(types.length == 0) {
                responseObj = methodToHandle.invoke(routeToHandle);
            } else if(types.length == 1 && types[0] == UpdateWrapper.class) {
                responseObj = methodToHandle.invoke(routeToHandle, wrapper);
            } else if(types.length == 2 && types[0] == UpdateWrapper.class && types[1] == I18NLabelsWrapper.class) {
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
            LOG.error("Error occurred while invoking the route {}:{} for message {}",
                    routeToHandle.getClass().getSimpleName(), methodToHandle.getName(), message);
            throw new RuntimeException(e);
        }
    }

    private void prepareSessionLocale(UpdateWrapper wrapper) {
        wrapper.getSession().set(SessionConstants.SESSION_LOCALE_KEY,
                Locale.forLanguageTag(wrapper.getUpdate().getMessage().getFrom().getLanguageCode()));
    }

    @Override
    public UpdateType getType() {
        return UpdateType.MESSAGE;
    }


    @Autowired
    public void setMessageRoutes(List<Route> messageRoutes) {
        this.messageRoutes = messageRoutes;
    }

    @Autowired
    public void setEntryPoint(Route entryPoint) {
        this.entryPoint = entryPoint;
    }
}
