package io.github.zhyshko.core.service;

public interface SessionService {

    Object get(Long userId, String key);

    void set(Long userId, String key, Object value);

    void delete(Long userId, String key);

    void invalidate(Long userId);

}
