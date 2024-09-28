package io.github.zhyshko.core.facade;

import io.github.zhyshko.core.util.UpdateWrapper;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateFacade {

    UpdateWrapper prepareUpdateWrapper(Update update);

}
