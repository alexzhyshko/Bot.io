package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.DocumentTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DocumentUpdateTypeProvider implements UpdateTypeProvider {

    private final DocumentTypePredicate documentTypePredicate;

    @Autowired
    public DocumentUpdateTypeProvider(DocumentTypePredicate documentTypePredicate) {
        this.documentTypePredicate = documentTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return documentTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.DOCUMENT;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

}
