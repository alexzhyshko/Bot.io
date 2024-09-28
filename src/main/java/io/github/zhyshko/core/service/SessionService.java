package io.github.zhyshko.core.service;

import io.github.zhyshko.core.util.UpdateWrapper;

public interface SessionService {

    Object get(UpdateWrapper wrapper, String key);


    void set(UpdateWrapper wrapper, String key, Object value);

    void delete(UpdateWrapper wrapper, String key);

    void invalidate(UpdateWrapper wrapper);

}
