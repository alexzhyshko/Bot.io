package io.github.zhyshko.core.response;

import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface ResponseExecutor {

    void execute(TelegramClient telegramClient, ResponseEntity responseEntity);

}
