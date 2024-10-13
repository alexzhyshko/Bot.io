package io.github.zhyshko.core.strategy;

import org.springframework.core.Ordered;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ChatIdRetrieveStrategy extends Ordered {

    boolean isApplicable(Update update);

    Long retrieve(Update update);
}
