package io.github.zhyshko.core.strategy.impl.document;

import io.github.zhyshko.core.predicate.impl.DocumentTypePredicate;
import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DocumentMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    private final DocumentTypePredicate documentTypePredicate;

    @Autowired
    public DocumentMessageIdRetrieveStrategy(DocumentTypePredicate documentTypePredicate) {
        this.documentTypePredicate = documentTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return documentTypePredicate.test(update);
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getMessage().getMessageId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
