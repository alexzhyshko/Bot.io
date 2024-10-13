package io.github.zhyshko.core.strategy;

import org.springframework.core.Ordered;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface PayloadRetrieveStrategy extends Ordered {

    boolean isApplicable(Update update);

    Object retrieve(Update update);

}
