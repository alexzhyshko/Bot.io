package io.github.zhyshko.core.strategy.impl;

import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageChatIdRetrieveStrategy implements ChatIdRetrieveStrategy {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public Long retrieve(Update update) {
        return update.getMessage().getChatId();
    }
}
