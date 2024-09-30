package io.github.zhyshko.core.session.impl;

import io.github.zhyshko.core.service.SessionService;

public class Session {

    private final Long userId;
    private final SessionService sessionService;

    protected Session(Long userId, SessionService sessionService) {
        this.userId = userId;
        this.sessionService = sessionService;
    }

    public Object get(String key) {
        return this.sessionService.get(userId, key);
    }

    public void set(String key, Object value) {
        this.sessionService.set(userId, key, value);
    }

    public void delete(String key) {
        this.sessionService.delete(userId, key);
    }

    public void invalidate() {
        this.sessionService.invalidate(userId);
    }

}
