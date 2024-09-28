package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackUpdateTypeProvider implements UpdateTypeProvider {

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public UpdateType get(Update update) {
        return UpdateType.CALLBACK;
    }
}
