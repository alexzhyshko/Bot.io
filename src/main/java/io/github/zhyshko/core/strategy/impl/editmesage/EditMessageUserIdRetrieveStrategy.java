package io.github.zhyshko.core.strategy.impl.editmesage;

import io.github.zhyshko.core.predicate.impl.EditMessageTypePredicate;
import io.github.zhyshko.core.strategy.UserIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditMessageUserIdRetrieveStrategy implements UserIdRetrieveStrategy {
    private final EditMessageTypePredicate editMessageTypePredicate;

    @Autowired
    public EditMessageUserIdRetrieveStrategy(EditMessageTypePredicate editMessageTypePredicate) {
        this.editMessageTypePredicate = editMessageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return editMessageTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getEditedMessage().getFrom().getId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
