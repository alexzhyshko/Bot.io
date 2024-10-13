package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.MessageTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUpdateTypeProvider implements UpdateTypeProvider {

    private final MessageTypePredicate messageTypePredicate;

    @Autowired
    public MessageUpdateTypeProvider(MessageTypePredicate messageTypePredicate) {
        this.messageTypePredicate = messageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return messageTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.MESSAGE;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
