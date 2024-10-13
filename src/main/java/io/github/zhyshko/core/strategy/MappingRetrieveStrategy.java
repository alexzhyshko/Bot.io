package io.github.zhyshko.core.strategy;

import org.springframework.core.Ordered;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MappingRetrieveStrategy extends Ordered {

    boolean isApplicable(Update update);

    String retrieve(Update update);

}
