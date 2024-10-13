package io.github.zhyshko.core.strategy.impl.document;

import io.github.zhyshko.core.predicate.impl.DocumentTypePredicate;
import io.github.zhyshko.core.strategy.MappingRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DocumentMappingRetrieveStrategy implements MappingRetrieveStrategy {
    private final DocumentTypePredicate documentTypePredicate;

    @Autowired
    public DocumentMappingRetrieveStrategy(DocumentTypePredicate documentTypePredicate) {
        this.documentTypePredicate = documentTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return documentTypePredicate.test(update);
    }

    @Override
    public String retrieve(Update update) {
        return update.getMessage().getDocument().getMimeType();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
