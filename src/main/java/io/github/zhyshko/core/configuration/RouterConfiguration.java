package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.router.UpdateRouter;
import io.github.zhyshko.core.util.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RouterConfiguration {

    private List<UpdateRouter> updateRouters;

    @Bean
    public Map<UpdateType, UpdateRouter> getUpdateRouters() {
        return updateRouters.stream()
                .collect(Collectors.toMap(UpdateRouter::getType, Function.identity()));
    }

    @Autowired
    public void setUpdateHandlers(List<UpdateRouter> updateRouters) {
        this.updateRouters = updateRouters;
    }

}
