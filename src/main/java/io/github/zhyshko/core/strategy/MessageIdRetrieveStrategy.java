package io.github.zhyshko.core.strategy;

import org.springframework.core.Ordered;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageIdRetrieveStrategy extends Ordered {

    boolean isApplicable(Update update);

    Integer retrieve(Update update);

}
