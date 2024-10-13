package io.github.zhyshko.core.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestWrapper {

    private String mapping;
    private UpdateType updateType;

}
