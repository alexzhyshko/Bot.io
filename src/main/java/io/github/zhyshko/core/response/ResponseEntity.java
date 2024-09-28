package io.github.zhyshko.core.response;

import io.github.zhyshko.core.router.Route;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ResponseEntity {

    @Singular
    private List<? extends BotApiMethod<? extends Serializable>> responses;

    private Class<? extends Route> nextRoute;

}
