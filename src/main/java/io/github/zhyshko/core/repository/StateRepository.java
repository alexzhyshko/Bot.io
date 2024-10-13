package io.github.zhyshko.core.repository;

public interface StateRepository {

    void updateStateByUserId(Integer state, Long userId);

    Integer findStateByUserId(Long userId);

}
