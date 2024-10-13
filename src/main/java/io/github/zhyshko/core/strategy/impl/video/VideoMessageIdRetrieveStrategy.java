package io.github.zhyshko.core.strategy.impl.video;

import io.github.zhyshko.core.predicate.impl.VideoTypePredicate;
import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VideoMessageIdRetrieveStrategy implements MessageIdRetrieveStrategy {
    private final VideoTypePredicate videoTypePredicate;

    @Autowired
    public VideoMessageIdRetrieveStrategy(VideoTypePredicate videoTypePredicate) {
        this.videoTypePredicate = videoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return videoTypePredicate.test(update);
    }

    @Override
    public Integer retrieve(Update update) {
        return update.getMessage().getMessageId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
