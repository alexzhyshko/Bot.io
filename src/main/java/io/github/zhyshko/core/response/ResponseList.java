package io.github.zhyshko.core.response;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ResponseList {

    @Singular
    private List<? extends BotApiMethod<? extends Serializable>> responses;

}
