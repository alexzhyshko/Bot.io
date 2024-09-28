package io.github.zhyshko.core.bot;

import io.github.zhyshko.core.configuration.ClientConfiguration;
import io.github.zhyshko.core.consumer.UpdatesConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
@RequiredArgsConstructor
public class RequestHandlerBot implements SpringLongPollingBot {

    private final UpdatesConsumer updatesConsumer;
    private final ClientConfiguration configuration;

    @Override
    public String getBotToken() {
        return configuration.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updatesConsumer;
    }
}
