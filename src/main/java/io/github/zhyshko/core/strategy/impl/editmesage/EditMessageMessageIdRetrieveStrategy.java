package io.github.zhyshko.core.strategy.impl.editmesage;

import io.github.zhyshko.core.predicate.impl.EditMessageTypePredicate;
import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditMessageMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    private final EditMessageTypePredicate editMessageTypePredicate;

    @Autowired
    public EditMessageMessageIdRetrieveStrategy(EditMessageTypePredicate editMessageTypePredicate) {
        this.editMessageTypePredicate = editMessageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return editMessageTypePredicate.test(update);
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getEditedMessage().getMessageId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
