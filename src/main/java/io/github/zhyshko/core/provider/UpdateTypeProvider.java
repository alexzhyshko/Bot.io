package io.github.zhyshko.core.provider;

import io.github.zhyshko.core.util.UpdateType;
import org.springframework.core.Ordered;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateTypeProvider extends Ordered {

    boolean isApplicable(Update update);

    UpdateType get();

}
