package io.github.zhyshko.core.repository.impl;

import io.github.zhyshko.core.repository.StateRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStateRepository implements StateRepository {

    private final Map<Long, Integer> states = new HashMap<>();

    @Override
    public void updateStateByUserId(Integer state, Long userId) {
        this.states.put(userId, state);
    }

    @Override
    public Integer findStateByUserId(Long userId) {
        return this.states.get(userId);
    }
}
