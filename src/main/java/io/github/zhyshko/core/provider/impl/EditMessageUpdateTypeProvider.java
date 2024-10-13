package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.EditMessageTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditMessageUpdateTypeProvider implements UpdateTypeProvider {

    private final EditMessageTypePredicate editMessageTypePredicate;

    @Autowired
    public EditMessageUpdateTypeProvider(EditMessageTypePredicate editMessageTypePredicate) {
        this.editMessageTypePredicate = editMessageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return editMessageTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.EDIT_MESSAGE;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
