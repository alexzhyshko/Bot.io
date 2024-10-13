package io.github.zhyshko.core.strategy.impl.photo;

import io.github.zhyshko.core.predicate.impl.PhotoTypePredicate;
import io.github.zhyshko.core.strategy.PayloadRetrieveStrategy;
import io.github.zhyshko.core.util.DocumentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PhotoPayloadRetrieveStrategy implements PayloadRetrieveStrategy {
    private final PhotoTypePredicate photoTypePredicate;

    @Autowired
    public PhotoPayloadRetrieveStrategy(PhotoTypePredicate photoTypePredicate) {
        this.photoTypePredicate = photoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return photoTypePredicate.test(update);
    }

    @Override
    public DocumentPayload retrieve(Update update) {
        return DocumentPayload.builder()
                .caption(update.getMessage().getCaption())
                .payload(update.getMessage().getPhoto())
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }
}
