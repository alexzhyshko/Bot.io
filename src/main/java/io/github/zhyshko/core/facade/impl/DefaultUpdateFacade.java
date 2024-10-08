package io.github.zhyshko.core.facade.impl;

import io.github.zhyshko.core.facade.UpdateFacade;
import io.github.zhyshko.core.provider.UpdateTypeProvider;
import io.github.zhyshko.core.service.StateService;
import io.github.zhyshko.core.strategy.ChatIdRetrieveStrategy;
import io.github.zhyshko.core.strategy.MessageIdRetrieveStrategy;
import io.github.zhyshko.core.strategy.UserIdRetrieveStrategy;
import io.github.zhyshko.core.util.UpdateType;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DefaultUpdateFacade implements UpdateFacade {

    private StateService stateService;
    private List<UserIdRetrieveStrategy> userIdRetrieveStrategies;
    private List<MessageIdRetrieveStrategy> messageIdRetrieveStrategies;
    private List<ChatIdRetrieveStrategy> chatIdRetrieveStrategies;
    private List<UpdateTypeProvider> updateTypeProviders;


    public UpdateWrapper prepareUpdateWrapper(Update update) {
        UpdateWrapper sessionWrapper = UpdateWrapper.builder()
                .updateType(getUpdateType(update))
                .userId(getUserId(update))
                .messageId(getMessageId(update))
                .chatId(getChatId(update))
                .update(update)
                .build();

        sessionWrapper.setState(this.stateService.getState(sessionWrapper.getUserId()));

        return sessionWrapper;
    }

    private Long getUserId(Update update) {
        return userIdRetrieveStrategies.stream()
                .filter(s -> s.isApplicable(update))
                .findFirst()
                .map(s -> s.retrieve(update))
                .orElseThrow(() -> new IllegalArgumentException("Could not get userId for update: "+update));
    }

    private Integer getMessageId(Update update) {
        return messageIdRetrieveStrategies.stream()
                .filter(s -> s.isApplicable(update))
                .findFirst()
                .map(s -> s.retrieve(update))
                .orElseThrow(() -> new IllegalArgumentException("Could not get messageId for update: "+update));
    }

    private Long getChatId(Update update) {
        return chatIdRetrieveStrategies.stream()
                .filter(s -> s.isApplicable(update))
                .findFirst()
                .map(s -> s.retrieve(update))
                .orElseThrow(() -> new IllegalArgumentException("Could not get chatId for update: "+update));
    }

    private UpdateType getUpdateType(Update update) {
        return updateTypeProviders.stream()
                .filter(s -> s.isApplicable(update))
                .findFirst()
                .map(UpdateTypeProvider::get)
                .orElseThrow(() -> new IllegalArgumentException("Update type is unsupported: "+update));
    }

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Autowired
    public void setUserIdRetrieveStrategies(List<UserIdRetrieveStrategy> userIdRetrieveStrategies) {
        this.userIdRetrieveStrategies = userIdRetrieveStrategies;
    }

    @Autowired
    public void setMessageIdRetrieveStrategies(List<MessageIdRetrieveStrategy> messageIdRetrieveStrategies) {
        this.messageIdRetrieveStrategies = messageIdRetrieveStrategies;
    }

    @Autowired
    public void setChatIdRetrieveStrategies(List<ChatIdRetrieveStrategy> chatIdRetrieveStrategies) {
        this.chatIdRetrieveStrategies = chatIdRetrieveStrategies;
    }

    @Autowired
    public void setUpdateTypeProviders(List<UpdateTypeProvider> updateTypeProviders) {
        this.updateTypeProviders = updateTypeProviders;
    }
}
