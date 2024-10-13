package io.github.zhyshko.core.predicate.impl;

import io.github.zhyshko.core.predicate.UpdateTypePredicate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackTypePredicate implements UpdateTypePredicate {

    @Override
    public boolean test(Update update) {
        return update.hasCallbackQuery();
    }
}
