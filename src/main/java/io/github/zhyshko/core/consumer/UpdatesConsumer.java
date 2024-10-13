package io.github.zhyshko.core.consumer;

import io.github.zhyshko.core.configuration.ConfigProperties;
import io.github.zhyshko.core.facade.UpdateFacade;
import io.github.zhyshko.core.filter.FilterExecutor;
import io.github.zhyshko.core.i18n.I18NWrapperPreparer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseExecutor;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.router.UpdateRouter;
import io.github.zhyshko.core.service.StateService;
import io.github.zhyshko.core.session.SessionExecutor;
import io.github.zhyshko.core.util.MethodUtil;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UpdatesConsumer implements LongPollingUpdateConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatesConsumer.class);

    private Map<UpdateType, UpdateRouter> updateRouters;
    private UpdateFacade updateFacade;
    private TelegramClient telegramClient;
    private ResponseExecutor responseExecutor;
    private StateService stateService;
    private FilterExecutor filterExecutor;
    private ApplicationContext applicationContext;
    private SessionExecutor sessionExecutor;
    private I18NWrapperPreparer i18NWrapperPreparer;
    private ConfigProperties config;
    private TaskExecutor threadPoolTaskExecutor;

    @Override
    public void consume(List<Update> updates) {
        LOG.debug("Received updates {}", updates);
        if(threadPoolTaskExecutor != null) {
            updates.forEach(u -> threadPoolTaskExecutor.execute(() -> this.consumeSingle(u)));
        } else {
            updates.forEach(this::consumeSingleInternal);
        }

    }

    private void consumeSingle(Update update) {
        if(config.isPerUserRequestLockingEnabled()) {
            consumeSingleInternalSynchronized(update);
        } else {
            consumeSingleInternal(update);
        }
    }

    private void consumeSingleInternalSynchronized(Update update) {
        Long chatId = this.updateFacade.getChatId(update);
        synchronized (chatId.toString().intern()) {
            consumeSingleInternal(update);
        }
    }

    private void consumeSingleInternal(Update update) {
        try {
            var updateWrapper = this.updateFacade.prepareUpdateWrapper(update);
            UpdateRouter router = Optional
                    .ofNullable(updateRouters.get(updateWrapper.getUpdateType()))
                    .orElseThrow(() -> new IllegalArgumentException("Update type is unsupported"));

            this.filterExecutor
                    .wrapWithFilters(wrapper -> sessionExecutor
                                    .wrapUnderSession((w, i) -> this.handleUpdateInternal(router, w, i), updateWrapper),
                            updateWrapper, telegramClient)
                    .ifPresent(response -> {
                        final I18NLabelsWrapper labelsWrapper = config.isI18nEnabled() ?
                                this.i18NWrapperPreparer.prepareWrapper(updateWrapper)
                                : null;
                        applyViewInitializer(updateWrapper, labelsWrapper, response);
                        this.responseExecutor.execute(telegramClient, response);
                        if (response.getNextRoute() != null) {
                            LOG.debug("For {} next route is {} ({})", updateWrapper.getUserId(),
                                    response.getNextRoute().getSimpleName(), response.getNextRoute().hashCode());
                            this.stateService.setState(response.getNextRoute().hashCode(), updateWrapper.getUserId());
                        }
                    });
        } catch (Exception e) {
            LOG.error("Error occurred while handling update {}", update, e);
        }
    }

    private Optional<ResponseEntity> handleUpdateInternal(UpdateRouter updateRouter,
                                                          UpdateWrapper wrapper, I18NLabelsWrapper i18nWrapper) {
        try {
            return updateRouter.handle(wrapper, i18nWrapper);
        } catch (Exception e) {
            LOG.error("Unhandled error executing update router", e);
            return Optional.empty();
        }
    }

    private void applyViewInitializer(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper,
                                      ResponseEntity responseEntity) {
        if (responseEntity.getNextRoute() == null) {
            return;
        }
        Route nextRoute = applicationContext.getBean(responseEntity.getNextRoute());

        invokeViewInitializer(nextRoute, wrapper, responseEntity, i18NLabelsWrapper);
    }

    private void invokeViewInitializer(Route nextRoute, UpdateWrapper wrapper,
                                       ResponseEntity responseEntity,
                                       I18NLabelsWrapper i18NLabelsWrapper) {
        MethodUtil.getViewInitializerMethod(nextRoute)
                .ifPresent(method -> {
                    try {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        Object responseObj = null;
                        if (paramTypes.length == 1 && paramTypes[0] == UpdateWrapper.class) {
                            responseObj = method.invoke(nextRoute, wrapper);
                        } else if (paramTypes.length == 2 && paramTypes[0] == UpdateWrapper.class
                                && paramTypes[1] == I18NLabelsWrapper.class) {
                            if(!config.isI18nEnabled()) {
                                throw new IllegalArgumentException(
                                        String.format("I18N is not enabled, but %s:%s requires i18nLabelsWrapper",
                                                nextRoute.getClass().getSimpleName(), method.getName()));
                            }
                            responseObj = method.invoke(nextRoute, wrapper, i18NLabelsWrapper);
                        } else {
                            LOG.warn("Method {} doesn't have applicable signature", method.getName());
                        }

                        if (responseObj instanceof ResponseList responseList) {
                            var responsesList = responseEntity.getResponses();
                            var additionalResponsesList = responseList.getResponses();
                            if (!additionalResponsesList.isEmpty()) {
                                List aggregatedList = new ArrayList<>();
                                aggregatedList.addAll(responsesList);
                                aggregatedList.addAll(additionalResponsesList);
                                responseEntity.setResponses(aggregatedList);
                            } else {
                                LOG.warn("View initializer {}:{} returned an empty response list",
                                        nextRoute.getClass().getSimpleName(), method.getName());
                            }
                        } else if (responseObj == null) {
                            LOG.warn("View initializer {}:{} returned a null response",
                                    nextRoute.getClass().getSimpleName(), method.getName());
                        } else {
                            LOG.warn("View initializer {}:{} returned an incorrect return type {}",
                                    nextRoute.getClass().getSimpleName(), method.getName(), responseObj);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOG.error("Error executing view initializer for {}", nextRoute.getClass().getSimpleName());
                        throw new RuntimeException(e);
                    }
                });
    }

    @Autowired
    public void setUpdateRouters(Map<UpdateType, UpdateRouter> updateRouters) {
        this.updateRouters = updateRouters;
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

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setSessionExecutor(SessionExecutor sessionExecutor) {
        this.sessionExecutor = sessionExecutor;
    }

    @Autowired
    public void setI18NWrapperPreparer(I18NWrapperPreparer i18NWrapperPreparer) {
        this.i18NWrapperPreparer = i18NWrapperPreparer;
    }

    @Autowired
    public void setConfig(ConfigProperties config) {
        this.config = config;
    }

    @Autowired
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        if(config != null && !config.isMultiThreadedUpdateConsumerEnabled()) {
            LOG.info("Mutlithreaded update consumer is disabled, proceeding in single-thread mode");
            this.threadPoolTaskExecutor = null;
            threadPoolTaskExecutor.shutdown();
        } else {
            LOG.info("Mutlithreaded update consumer is enabled");
            this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        }
    }
}
