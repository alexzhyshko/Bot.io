package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.CallbackTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackUpdateTypeProvider implements UpdateTypeProvider {

    private final CallbackTypePredicate callbackTypePredicate;

    @Autowired
    public CallbackUpdateTypeProvider(CallbackTypePredicate callbackTypePredicate) {
        this.callbackTypePredicate = callbackTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return callbackTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.CALLBACK;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
