package io.github.zhyshko.core.util;

import io.github.zhyshko.core.session.impl.Session;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class UpdateWrapper {

    private Long userId;
    private Long chatId;
    private Integer messageId;
    private Integer state;
    private Update update;
    private String mapping;
    private UpdateType updateType;
    private Session session;
    private Exception exception;
}
