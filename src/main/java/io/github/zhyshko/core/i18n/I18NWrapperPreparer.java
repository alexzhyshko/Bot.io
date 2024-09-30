package io.github.zhyshko.core.i18n;

import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.stereotype.Component;

@Component
public interface I18NWrapperPreparer {

    I18NLabelsWrapper prepareWrapper(UpdateWrapper wrapper);

}
