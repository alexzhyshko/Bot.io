package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.VideoTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VideoUpdateTypeProvider implements UpdateTypeProvider {

    private final VideoTypePredicate videoTypePredicate;

    @Autowired
    public VideoUpdateTypeProvider(VideoTypePredicate videoTypePredicate) {
        this.videoTypePredicate = videoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return videoTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.VIDEO;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }

}
