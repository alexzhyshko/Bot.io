package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.provider.BotTokenProvider;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;
import java.util.Optional;

@Getter
@Component
public class ClientConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    private String botToken;

    @Autowired
    public ClientConfiguration(ConfigProperties config, Optional<BotTokenProvider> botTokenProvider) {
        botTokenProvider.ifPresentOrElse(
                provider -> {
                    this.botToken = provider.provide();
                    LOG.info("Loaded bot token using custom provider {}", provider.getClass().getSimpleName());
                }, () -> {
                    if (config.getBotToken() != null) {
                        this.botToken = config.getBotToken();
                        LOG.info("Loaded bot token from properties");
                    } else {
                        String token = System.getenv("BOT_TOKEN");
                        if (Objects.isNull(token)) {
                            LOG.error("Bot Token not found in any of supported sources");
                            throw new IllegalArgumentException("Bot Token not found in any of supported sources");
                        }
                        this.botToken = token;
                        LOG.info("Loaded bot token from environment");
                    }
                }
        );
    }

    @Bean
    public TelegramClient getTelegramClient() {
        return new OkHttpTelegramClient(getBotToken());
    }
}
