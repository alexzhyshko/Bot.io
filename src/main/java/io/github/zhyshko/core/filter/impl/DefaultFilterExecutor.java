package io.github.zhyshko.core.filter.impl;

import io.github.zhyshko.core.exception.FilterException;
import io.github.zhyshko.core.filter.FilterAdapter;
import io.github.zhyshko.core.filter.FilterExecutor;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class DefaultFilterExecutor implements FilterExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFilterExecutor.class);

    private List<FilterAdapter> filters;

    @Override
    public Optional<ResponseEntity> wrapWithFilters(Function<UpdateWrapper, Optional<ResponseEntity>> function,
                                                    UpdateWrapper wrapper, TelegramClient telegramClient) {
        try {
            FilterChain filterChain = new FilterChain(function, filters, wrapper, telegramClient);
            filterChain.doFilter();
            return filterChain.getResult();
        } catch (FilterException fe) {
            throw fe;
        }
    }

    @Autowired
    public void setFilters(List<FilterAdapter> filters) {
        this.filters = filters;
    }
}
