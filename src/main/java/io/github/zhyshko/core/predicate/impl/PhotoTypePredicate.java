package io.github.zhyshko.core.predicate.impl;

import io.github.zhyshko.core.predicate.UpdateTypePredicate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class PhotoTypePredicate implements UpdateTypePredicate {

    @Override
    public boolean test(Update update) {
        return update.hasMessage() && Objects.nonNull(update.getMessage().getPhoto());
    }
}
