package io.github.zhyshko.core.strategy.impl;

import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getCallbackQuery().getMessage().getMessageId();
    }
}
