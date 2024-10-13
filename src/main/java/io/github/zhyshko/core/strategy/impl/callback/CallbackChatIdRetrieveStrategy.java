package io.github.zhyshko.core.strategy.impl.callback;

import io.github.zhyshko.core.predicate.impl.CallbackTypePredicate;
import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackChatIdRetrieveStrategy implements ChatIdRetrieveStrategy {

    private final CallbackTypePredicate callbackTypePredicate;

    @Autowired
    public CallbackChatIdRetrieveStrategy(CallbackTypePredicate callbackTypePredicate) {
        this.callbackTypePredicate = callbackTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return callbackTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
