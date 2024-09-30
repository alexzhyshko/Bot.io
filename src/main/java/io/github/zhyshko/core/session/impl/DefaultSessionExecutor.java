package io.github.zhyshko.core.session.impl;

import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.service.SessionService;
import io.github.zhyshko.core.session.SessionExecutor;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class DefaultSessionExecutor implements SessionExecutor {

    private SessionService sessionService;

    @Override
    public Optional<ResponseEntity> wrapUnderSession(Function<UpdateWrapper, Optional<ResponseEntity>> routerFunction,
                                                     UpdateWrapper wrapper) {

        Session session = new Session(wrapper.getUserId(), sessionService);
        wrapper.setSession(session);
        return routerFunction.apply(wrapper);
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
