package io.github.zhyshko.core.filter;

import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;
import java.util.function.Function;

public interface FilterExecutor {

    Optional<ResponseEntity> wrapWithFilters(Function<UpdateWrapper, Optional<ResponseEntity>> function,
                        UpdateWrapper wrapper, TelegramClient telegramClient);

}
