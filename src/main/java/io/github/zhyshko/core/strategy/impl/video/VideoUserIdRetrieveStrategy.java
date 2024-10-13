package io.github.zhyshko.core.strategy.impl.video;

import io.github.zhyshko.core.predicate.impl.VideoTypePredicate;
import io.github.zhyshko.core.strategy.UserIdRetrieveStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VideoUserIdRetrieveStrategy implements UserIdRetrieveStrategy {
    private final VideoTypePredicate videoTypePredicate;

    @Autowired
    public VideoUserIdRetrieveStrategy(VideoTypePredicate videoTypePredicate) {
        this.videoTypePredicate = videoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return videoTypePredicate.test(update);
    }

    @Override
    public Long retrieve(Update update) {
        return update.getMessage().getFrom().getId();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
