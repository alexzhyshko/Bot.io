package io.github.zhyshko.core.consumer;

import io.github.zhyshko.core.facade.UpdateFacade;
import io.github.zhyshko.core.filter.FilterExecutor;
import io.github.zhyshko.core.response.ResponseExecutor;
import io.github.zhyshko.core.router.UpdateRouter;
import io.github.zhyshko.core.service.StateService;
import io.github.zhyshko.core.util.UpdateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;

@Component
public class UpdatesConsumer implements LongPollingUpdateConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatesConsumer.class);

    private Map<UpdateType, UpdateRouter> updateHandlers;
    private UpdateFacade updateFacade;
    private TelegramClient telegramClient;
    private ResponseExecutor responseExecutor;
    private StateService stateService;
    private FilterExecutor filterExecutor;

    @Override
    public void consume(List<Update> updates) {
        LOG.info("Received updates {}", updates);
        updates.forEach(this::consumeSingle);
    }

    private void consumeSingle(Update update) {
        var updateWrapper = this.updateFacade.prepareUpdateWrapper(update);
        try {
            UpdateRouter router = updateHandlers.get(updateWrapper.getUpdateType());
            this.filterExecutor.wrapWithFilters(router::handle, updateWrapper, telegramClient)
                    .ifPresent(response -> {
                        this.responseExecutor.execute(telegramClient, response);
                        if (response.getNextRoute() != null) {
                            LOG.info("For {} next route is {} ({})", updateWrapper.getUserId(),
                                    response.getNextRoute().getSimpleName(), response.getNextRoute().hashCode());
                            this.stateService.setState(response.getNextRoute().hashCode(), updateWrapper.getUserId());
                        }
                    });
        } catch (Exception e) {
            LOG.error("Error occurred while handling update {}", updateWrapper.getUpdate(), e);
        }

    }

    @Autowired
    public void setUpdateHandlers(Map<UpdateType, UpdateRouter> updateHandlers) {
        this.updateHandlers = updateHandlers;
    }

    @Autowired
    public void setUpdateFacade(UpdateFacade updateFacade) {
        this.updateFacade = updateFacade;
    }

    @Autowired
    public void setTelegramClient(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Autowired
    public void setResponseExecutor(ResponseExecutor responseExecutor) {
        this.responseExecutor = responseExecutor;
    }

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Autowired
    public void setFilterExecutor(FilterExecutor filterExecutor) {
        this.filterExecutor = filterExecutor;
    }
}
