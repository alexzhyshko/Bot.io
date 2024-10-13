package io.github.zhyshko.core.repository.impl;

import io.github.zhyshko.core.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemorySessionRepository implements SessionRepository {

    private final Map<Long, Map<String, Object>> sessionAttributes = new HashMap<>();

    @Override
    public void set(Long userId, String key, Object value) {
        if(!this.sessionAttributes.containsKey(userId)) {
            this.sessionAttributes.put(userId, new HashMap<>());
        }
        var userSession = this.sessionAttributes.get(userId);
        userSession.put(key, value);
    }

    @Override
    public Object get(Long userId, String key) {
        if(!this.sessionAttributes.containsKey(userId)) {
            return null;
        }
        return this.sessionAttributes.get(userId).get(key);
    }

    @Override
    public void delete(Long userId, String key) {
        if(!this.sessionAttributes.containsKey(userId)) {
            return;
        }
        this.sessionAttributes.get(userId).remove(key);
    }

    @Override
    public void invalidate(Long userId) {
        this.sessionAttributes.remove(userId);
    }
}
