package io.github.zhyshko.core.strategy;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserIdRetrieveStrategy {

    boolean isApplicable(Update update);

    Long retrieve(Update update);

}
