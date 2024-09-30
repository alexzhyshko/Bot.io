package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUpdateTypeProvider implements UpdateTypeProvider {

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public UpdateType get() {
        return UpdateType.MESSAGE;
    }
}
