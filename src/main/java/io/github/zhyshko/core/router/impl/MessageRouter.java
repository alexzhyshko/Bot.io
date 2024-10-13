package io.github.zhyshko.core.router.impl;

import io.github.zhyshko.core.constants.SessionConstants;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageRouter extends AbstractRouter {

    private final Route entryPoint;

    @Autowired
    public MessageRouter(Route entryPoint) {
        this.entryPoint = entryPoint;
    }

    @Override
    public void beforeHandle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
        var state = wrapper.getState();
        if (state == null) {
            this.prepareSessionLocale(wrapper);
        }
    }

    @Override
    public Route getRouteToHandle(UpdateWrapper wrapper) {
        var state = wrapper.getState();
        if (state == null) {
            return entryPoint;
        }
        return super.getRouteToHandle(wrapper);
    }

    private void prepareSessionLocale(UpdateWrapper wrapper) {
        wrapper.getSession().set(SessionConstants.SESSION_LOCALE_KEY,
                Locale.forLanguageTag(wrapper.getUpdate().getMessage().getFrom().getLanguageCode()));
    }

    @Override
    public UpdateType getType() {
        return UpdateType.MESSAGE;
    }

}
