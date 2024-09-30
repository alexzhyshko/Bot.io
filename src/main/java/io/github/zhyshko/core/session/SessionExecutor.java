package io.github.zhyshko.core.session;

import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateWrapper;

import java.util.Optional;
import java.util.function.Function;

public interface SessionExecutor {

    Optional<ResponseEntity> wrapUnderSession(Function<UpdateWrapper, Optional<ResponseEntity>> routerFunction,
                                              UpdateWrapper wrapper);

}
