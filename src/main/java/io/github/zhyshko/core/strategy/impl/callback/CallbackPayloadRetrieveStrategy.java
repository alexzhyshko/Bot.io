package io.github.zhyshko.core.strategy.impl.callback;

import io.github.zhyshko.core.predicate.impl.CallbackTypePredicate;
import io.github.zhyshko.core.strategy.PayloadRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackPayloadRetrieveStrategy implements PayloadRetrieveStrategy {
    private final CallbackTypePredicate callbackTypePredicate;

    @Autowired
    public CallbackPayloadRetrieveStrategy(CallbackTypePredicate callbackTypePredicate) {
        this.callbackTypePredicate = callbackTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return callbackTypePredicate.test(update);
    }

    @Override
    public String retrieve(Update update) {
        return update.getCallbackQuery().getData();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
