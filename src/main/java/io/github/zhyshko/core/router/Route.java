package io.github.zhyshko.core.router;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Route {

    void initView(SendMessage.SendMessageBuilder builder);

}
