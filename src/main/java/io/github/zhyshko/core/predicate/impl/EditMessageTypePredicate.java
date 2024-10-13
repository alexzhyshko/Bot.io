package io.github.zhyshko.core.predicate.impl;

import io.github.zhyshko.core.predicate.UpdateTypePredicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditMessageTypePredicate implements UpdateTypePredicate {

    @Override
    public boolean test(Update update) {
        return update.hasEditedMessage() && StringUtils.isNotBlank(update.getEditedMessage().getText());
    }
}
