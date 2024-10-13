package io.github.zhyshko.core.strategy.impl.message;

import io.github.zhyshko.core.predicate.impl.MessageTypePredicate;
import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    private final MessageTypePredicate messageTypePredicate;

    @Autowired
    public MessageMessageIdRetrieveStrategy(MessageTypePredicate messageTypePredicate) {
        this.messageTypePredicate = messageTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return messageTypePredicate.test(update);
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getMessage().getMessageId();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
