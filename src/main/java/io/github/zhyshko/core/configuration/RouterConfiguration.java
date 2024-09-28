package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.annotation.Callback;
import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterConfiguration {

    private ApplicationContext applicationContext;

    @Bean
    public List<Route> messageRoutes() {
        return applicationContext.getBeansWithAnnotation(Message.class)
                .values()
                .stream()
                .map(o -> (Route) o)
                .toList();
    }

    @Bean
    public List<Route> callbackRoutes() {
        return applicationContext.getBeansWithAnnotation(Callback.class)
                .values()
                .stream()
                .map(o -> (Route) o)
                .toList();
    }

    @Bean("entryPoint")
    @Primary
    public Route getEntryPoint() {
        return applicationContext.getBeansWithAnnotation(Entrypoint.class)
                .values()
                .stream()
                .map(o -> (Route) o)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Please, annotate your entrypoint route with @Entrypoint"));
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
