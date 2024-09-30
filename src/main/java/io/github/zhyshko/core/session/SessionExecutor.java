package io.github.zhyshko.core.session;

import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateWrapper;

import java.util.Optional;
import java.util.function.BiFunction;

public interface SessionExecutor {

    Optional<ResponseEntity> wrapUnderSession(
            BiFunction<UpdateWrapper, I18NLabelsWrapper, Optional<ResponseEntity>> routerFunction,
            UpdateWrapper wrapper);

}
