package io.github.zhyshko.core.filter.impl;

import io.github.zhyshko.core.exception.FilterException;
import io.github.zhyshko.core.filter.FilterAdapter;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FilterChain {

    private final Function<UpdateWrapper, Optional<ResponseEntity>> handler;
    private final List<FilterAdapter> filters;
    private final UpdateWrapper updateWrapper;
    private final TelegramClient telegramClient;
    private Optional<ResponseEntity> responseEntity;
    private int filterPosition = -1;

    public FilterChain(Function<UpdateWrapper, Optional<ResponseEntity>> handler,
                       List<FilterAdapter> filters,
                       UpdateWrapper updateWrapper,
                       TelegramClient telegramClient) {
        this.handler = handler;
        this.filters = filters;
        this.updateWrapper = updateWrapper;
        this.telegramClient = telegramClient;
    }

    public void doFilter() {
        if (filterPosition < filters.size() - 1) {
            filterPosition++;
            FilterAdapter nextFilter = filters.get(filterPosition);
            try {
                nextFilter.filter(this, updateWrapper, telegramClient);
            } catch (FilterException fe) {
                throw fe;
            }
        } else {
            responseEntity = this.handler.apply(updateWrapper);
        }
    }

    public Optional<ResponseEntity> getResult() {
        return responseEntity;
    }

}
