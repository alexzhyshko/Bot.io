package io.github.zhyshko.core.router;

import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;

import java.util.Optional;

public interface UpdateRouter {

    Optional<ResponseEntity> handle(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper);

    UpdateType getType();

}
