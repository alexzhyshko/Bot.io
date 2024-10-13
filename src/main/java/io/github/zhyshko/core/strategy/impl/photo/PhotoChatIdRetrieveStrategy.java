package io.github.zhyshko.core.strategy.impl.photo;

import io.github.zhyshko.core.predicate.impl.PhotoTypePredicate;
import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PhotoChatIdRetrieveStrategy implements ChatIdRetrieveStrategy {

    private final PhotoTypePredicate photoTypePredicate;

    @Autowired
    public PhotoChatIdRetrieveStrategy(PhotoTypePredicate photoTypePredicate) {
        this.photoTypePredicate = photoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return photoTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getMessage().getChatId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }
}
