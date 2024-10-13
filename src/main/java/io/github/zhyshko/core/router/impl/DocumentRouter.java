package io.github.zhyshko.core.router.impl;

import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentRouter extends AbstractRouter {

    @Override
    public void beforeHandle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
        var state = wrapper.getState();
        if (state == null) {
            throw new IllegalArgumentException("User can't use send document routes yet - state is null, " +
                    "please first use entrypoint");
        }
    }

    @Override
    public UpdateType getType() {
        return UpdateType.DOCUMENT;
    }

}
