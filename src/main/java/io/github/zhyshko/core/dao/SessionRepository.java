package io.github.zhyshko.core.dao;

public interface SessionRepository {

    void set(Long userId, String key, Object value);

    Object get(Long userId, String key);

    void delete(Long userId, String key);

    void invalidate(Long userId);

}