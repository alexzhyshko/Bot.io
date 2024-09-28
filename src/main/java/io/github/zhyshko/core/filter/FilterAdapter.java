package io.github.zhyshko.core.filter;

import io.github.zhyshko.core.exception.FilterException;
import io.github.zhyshko.core.filter.impl.FilterChain;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface FilterAdapter {

    void filter(FilterChain filterChain, UpdateWrapper wrapper, TelegramClient telegramClient) throws FilterException;

}
