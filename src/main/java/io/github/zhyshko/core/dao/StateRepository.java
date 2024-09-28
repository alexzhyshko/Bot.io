package io.github.zhyshko.core.dao;

public interface StateRepository {

    void updateStateByUserId(Integer state, Long userId);

    Integer findStateByUserId(Long userId);

}
