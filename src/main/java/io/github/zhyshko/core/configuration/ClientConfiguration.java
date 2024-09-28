package io.github.zhyshko.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;

@Component
public class ClientConfiguration {

    private final String botToken;

    public ClientConfiguration() {
        String token = System.getenv("BOT_TOKEN");
        if(Objects.isNull(token)) {
            throw new IllegalArgumentException("Bot Token is null");
        }
        this.botToken = token;
    }

    @Bean
    public TelegramClient getTelegramClient() {
        return new OkHttpTelegramClient(getBotToken());
    }

    public String getBotToken() {
        return botToken;
    }

}
