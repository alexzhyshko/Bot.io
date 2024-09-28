package io.github.zhyshko.core.router;

import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;

import java.util.Optional;

public interface UpdateRouter {

    Optional<ResponseEntity> handle(UpdateWrapper wrapper);

    UpdateType getType();

}
