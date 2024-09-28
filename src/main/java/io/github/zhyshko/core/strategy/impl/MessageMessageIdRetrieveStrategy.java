package io.github.zhyshko.core.strategy.impl;

import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getMessage().getMessageId();
    }
}
