package io.github.zhyshko.core.strategy.impl.video;

import io.github.zhyshko.core.predicate.impl.VideoTypePredicate;
import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VideoChatIdRetrieveStrategy implements ChatIdRetrieveStrategy {

    private final VideoTypePredicate videoTypePredicate;

    @Autowired
    public VideoChatIdRetrieveStrategy(VideoTypePredicate videoTypePredicate) {
        this.videoTypePredicate = videoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return videoTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getMessage().getChatId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
