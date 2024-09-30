package io.github.zhyshko.core.i18n.impl;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18NLabelsWrapper {

    private final Locale locale;
    private final MessageSource messageSource;

    protected I18NLabelsWrapper(Locale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;
    }

    public String getLabel(String key, String[] args) {
        return messageSource.getMessage(key, args, locale);
    }

    public String getLabel(String key) {
        return messageSource.getMessage(key, null, locale);
    }
}
