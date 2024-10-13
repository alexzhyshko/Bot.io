package io.github.zhyshko.core.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentPayload {

    private Object payload;
    private String caption;

}
