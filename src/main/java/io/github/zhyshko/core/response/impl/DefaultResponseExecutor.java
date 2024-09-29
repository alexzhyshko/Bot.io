package io.github.zhyshko.core.response.impl;

import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@Component
public class DefaultResponseExecutor implements ResponseExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultResponseExecutor.class);

    @Override
    public void execute(TelegramClient telegramClient, ResponseEntity responseEntity) {
        if(responseEntity == null) {
            LOG.info("No response provided");
            return;
        }
        responseEntity.getResponses().forEach(m -> executeMethod(telegramClient, m));
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void executeMethod(TelegramClient telegramClient,
                                                                                        Method method) {
        try {
            telegramClient.execute(method);
        } catch (Exception e) {
            LOG.error("Error during executing {}", method, e);
        }
    }
}
