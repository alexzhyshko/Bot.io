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
        Locale locale = Optional.ofNullable(wrapper.getSession())
                .map(s -> s.get(SessionConstants.SESSION_LOCALE_KEY))
                .filter(Locale.class::isInstance)
                .map(Locale.class::cast)
                .orElse(Optional.ofNullable(wrapper.getUpdate())
                        .filter(Update::hasMessage)
                        .map(Update::getMessage)
                        .map(m -> m.getFrom().getLanguageCode())
                        .map(Locale::forLanguageTag)
                        .orElse(Locale.ENGLISH));
        return new I18NLabelsWrapper(locale, messageSource);
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
