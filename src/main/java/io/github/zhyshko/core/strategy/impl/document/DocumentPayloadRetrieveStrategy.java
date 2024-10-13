package io.github.zhyshko.core.strategy.impl.document;

import io.github.zhyshko.core.predicate.impl.DocumentTypePredicate;
import io.github.zhyshko.core.strategy.PayloadRetrieveStrategy;
import io.github.zhyshko.core.util.DocumentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DocumentPayloadRetrieveStrategy implements PayloadRetrieveStrategy {
    private final DocumentTypePredicate documentTypePredicate;

    @Autowired
    public DocumentPayloadRetrieveStrategy(DocumentTypePredicate documentTypePredicate) {
        this.documentTypePredicate = documentTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return documentTypePredicate.test(update);
    }

    @Override
    public DocumentPayload retrieve(Update update) {
        return DocumentPayload.builder()
                .caption(update.getMessage().getCaption())
                .payload(update.getMessage().getDocument())
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
