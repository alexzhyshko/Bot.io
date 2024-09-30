package io.github.zhyshko.core.service.impl;

import io.github.zhyshko.core.dao.SessionRepository;
import io.github.zhyshko.core.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSessionService implements SessionService {

    private SessionRepository sessionRepository;

    @Override
    public Object get(Long userId, String key) {
        return this.sessionRepository.get(userId, key);
    }

    @Override
    public void set(Long userId, String key, Object value) {
        this.sessionRepository.set(userId, key, value);
    }

    @Override
    public void delete(Long userId, String key) {
        this.sessionRepository.delete(userId, key);
    }

    @Override
    public void invalidate(Long userId) {
        this.sessionRepository.invalidate(userId);
    }

    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
}
