package io.github.zhyshko.core.strategy;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageIdRetrieveStrategy {

    boolean isApplicable(Update update);

    Integer retrieve(Update update);

}
