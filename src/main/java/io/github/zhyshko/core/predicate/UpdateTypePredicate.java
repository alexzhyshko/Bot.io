package io.github.zhyshko.core.predicate;

import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface UpdateTypePredicate {

    boolean test(Update update);

}
