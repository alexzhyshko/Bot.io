package io.github.zhyshko.core.strategy.impl.callback;

import io.github.zhyshko.core.predicate.impl.CallbackTypePredicate;
import io.github.zhyshko.core.strategy.UserIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackUserIdRetrieveStrategy implements UserIdRetrieveStrategy {
    private final CallbackTypePredicate callbackTypePredicate;

    @Autowired
    public CallbackUserIdRetrieveStrategy(CallbackTypePredicate callbackTypePredicate) {
        this.callbackTypePredicate = callbackTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return callbackTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getCallbackQuery().getFrom().getId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
