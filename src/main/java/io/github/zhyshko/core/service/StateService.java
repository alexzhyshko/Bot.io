package io.github.zhyshko.core.service;

public interface StateService {

    Integer getState(Long userId);

    void setState(Integer state, Long userId);

}
