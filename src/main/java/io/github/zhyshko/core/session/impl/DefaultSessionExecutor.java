package io.github.zhyshko.core.session.impl;

import io.github.zhyshko.core.configuration.ConfigProperties;
import io.github.zhyshko.core.i18n.I18NWrapperPreparer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.service.SessionService;
import io.github.zhyshko.core.session.SessionExecutor;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class DefaultSessionExecutor implements SessionExecutor {

    private SessionService sessionService;
    private I18NWrapperPreparer i18NWrapperPreparer;
    private ConfigProperties config;

    @Override
    public Optional<ResponseEntity> wrapUnderSession(
            BiFunction<UpdateWrapper, I18NLabelsWrapper, Optional<ResponseEntity>> routerFunction,
            UpdateWrapper wrapper) {

        Session session = new Session(wrapper.getUserId(), sessionService);
        wrapper.setSession(session);

        final I18NLabelsWrapper labelsWrapper = config.isI18nEnabled() ?
                this.i18NWrapperPreparer.prepareWrapper(wrapper)
                : null;

        return routerFunction.apply(wrapper, labelsWrapper);
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setI18NWrapperPreparer(I18NWrapperPreparer i18NWrapperPreparer) {
        this.i18NWrapperPreparer = i18NWrapperPreparer;
    }

    @Autowired
    public void setConfig(ConfigProperties config) {
        this.config = config;
    }
}
