package io.github.zhyshko.core.strategy.impl.editmesage;

import io.github.zhyshko.core.predicate.impl.EditMessageTypePredicate;
import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditMessageChatIdRetrieveStrategy implements ChatIdRetrieveStrategy {

    private final EditMessageTypePredicate editMessageTypePredicate;

    @Autowired
    public EditMessageChatIdRetrieveStrategy(EditMessageTypePredicate editMessageTypePredicate) {
        this.editMessageTypePredicate = editMessageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return editMessageTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getEditedMessage().getChatId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
