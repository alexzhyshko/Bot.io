package io.github.zhyshko.core.provider.impl;

import io.github.zhyshko.core.predicate.impl.PhotoTypePredicate;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PhotoUpdateTypeProvider implements UpdateTypeProvider {

    private final PhotoTypePredicate photoTypePredicate;

    @Autowired
    public PhotoUpdateTypeProvider(PhotoTypePredicate photoTypePredicate) {
        this.photoTypePredicate = photoTypePredicate;
    }

    @Override
    public boolean isApplicable(Update update) {
        return photoTypePredicate.test(update);
    }

    @Override
    public UpdateType get() {
        return UpdateType.PHOTO;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }

}
