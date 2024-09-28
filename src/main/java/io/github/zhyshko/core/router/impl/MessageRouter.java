package io.github.zhyshko.core.router.impl;

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
import java.util.Optional;

@Component
public class MessageRouter implements UpdateRouter {

    private static final Logger LOG = LoggerFactory.getLogger(MessageRouter.class);

    private List<Route> messageRoutes;
    private Route entryPoint;


    @Override
    public Optional<ResponseEntity> handle(UpdateWrapper wrapper) {
        var state = wrapper.getState();
        Route routeToHandle;
        if (state == null) {
            routeToHandle = entryPoint;
        } else {
            routeToHandle = messageRoutes.stream()
                    .filter(r -> r.getClass().hashCode() == state)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Route for given path: " + state + " not found"));
        }

        var message = wrapper.getUpdate().getMessage().getText();

        Method methodToHandle = MethodUtil.getMethodForMessage(routeToHandle, message);

        LOG.info("Routing {} to {}:{}",
                wrapper.getUserId(), routeToHandle.getClass().getSimpleName(), methodToHandle.getName());

        try {
            Object responseObj = methodToHandle.invoke(routeToHandle, wrapper);
            if (responseObj instanceof ResponseEntity responseEntity) {
                return Optional.of(responseEntity);
            }
            LOG.warn("Method {}:{} returned unexpected return type {}",
                    routeToHandle.getClass().getSimpleName(), methodToHandle.getName(), responseObj.getClass());
            return Optional.empty();
        } catch (Exception e) {
            LOG.error("Error occurred while invoking the route {}:{} for message {}",
                    routeToHandle.getClass().getSimpleName(), methodToHandle.getName(), message);
            throw new RuntimeException(e);
        }
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
