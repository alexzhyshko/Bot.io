package io.github.zhyshko.core.service.impl;

import io.github.zhyshko.core.repository.StateRepository;
import io.github.zhyshko.core.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultStateService implements StateService {

    private StateRepository stateRepository;

    @Override
    public Integer getState(Long userId) {
        return this.stateRepository.findStateByUserId(userId);
    }

    @Override
    public void setState(Integer state, Long userId) {
        this.stateRepository.updateStateByUserId(state, userId);
    }

    @Autowired
    public void setStateRepository(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }
}
