package io.github.zhyshko.core.strategy.impl;

import io.github.zhyshko.core.strategy.UserIdRetrieveStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUserIdRetrieveStrategy implements UserIdRetrieveStrategy {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public Long retrieve(Update update) {
        return update.getMessage().getFrom().getId();
    }
}