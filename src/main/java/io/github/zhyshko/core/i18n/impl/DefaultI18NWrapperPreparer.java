package io.github.zhyshko.core.i18n.impl;

import io.github.zhyshko.core.constants.SessionConstants;
import io.github.zhyshko.core.i18n.I18NWrapperPreparer;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.Optional;

@Component
public class DefaultI18NWrapperPreparer implements I18NWrapperPreparer {

    private MessageSource messageSource;

    @Override
    public I18NLabelsWrapper prepareWrapper(UpdateWrapper wrapper) {
        String langCode = Optional.ofNullable(wrapper.getSession())
                .map(s -> s.get(SessionConstants.SESSION_LOCALE_KEY))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(Optional.ofNullable(wrapper.getUpdate())
                        .filter(Update::hasMessage)
                        .map(Update::getMessage)
                        .map(m -> m.getFrom().getLanguageCode())
                        .orElse("en"));
        return new I18NLabelsWrapper(Locale.forLanguageTag(langCode),
                messageSource);
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
