package io.github.zhyshko.core.service.impl;

import io.github.zhyshko.core.dao.SessionRepository;
import io.github.zhyshko.core.service.SessionService;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSessionService implements SessionService {

    private SessionRepository sessionRepository;


    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Object get(UpdateWrapper wrapper, String key) {
        return this.sessionRepository.get(wrapper.getUserId(), key);
    }

    @Override
    public void set(UpdateWrapper wrapper, String key, Object value) {
        this.sessionRepository.set(wrapper.getUserId(), key, value);
    }

    @Override
    public void delete(UpdateWrapper wrapper, String key) {
        this.sessionRepository.delete(wrapper.getUserId(), key);
    }

    @Override
    public void invalidate(UpdateWrapper wrapper) {
        this.sessionRepository.invalidate(wrapper.getUserId());
    }
}
