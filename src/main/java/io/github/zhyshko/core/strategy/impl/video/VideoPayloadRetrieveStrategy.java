package io.github.zhyshko.core.strategy.impl.video;

import io.github.zhyshko.core.predicate.impl.VideoTypePredicate;
import io.github.zhyshko.core.strategy.PayloadRetrieveStrategy;
import io.github.zhyshko.core.util.DocumentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VideoPayloadRetrieveStrategy implements PayloadRetrieveStrategy {
    private final VideoTypePredicate videoTypePredicate;

    @Autowired
    public VideoPayloadRetrieveStrategy(VideoTypePredicate videoTypePredicate) {
        this.videoTypePredicate = videoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return videoTypePredicate.test(update);
    }

    @Override
    public DocumentPayload retrieve(Update update) {
        return DocumentPayload.builder()
                .caption(update.getMessage().getCaption())
                .payload(update.getMessage().getVideo())
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
