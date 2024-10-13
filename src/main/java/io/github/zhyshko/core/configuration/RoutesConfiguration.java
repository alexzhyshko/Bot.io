package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoutesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RoutesConfiguration.class);

    private ApplicationContext applicationContext;

    @Bean
    public List<Route> routes(List<Route> routes) {
        routes = routes.stream().distinct().toList();
        if (!routes.isEmpty()) {
            LOG.info("Found and initialized {} route classes", routes.size());
            if (LOG.isDebugEnabled()) {
                LOG.info("Route classes found: {}", routes);
            }
        }
        return routes;
    }

    @Bean("entryPoint")
    @Primary
    public Route getEntryPoint() {
        Route entryPoint = applicationContext.getBeansWithAnnotation(Entrypoint.class)
                .values()
                .stream()
                .map(o -> (Route) o)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Please, annotate your entrypoint route with @Entrypoint"));

        LOG.info("Found entrypoint class {}", entryPoint.getClass().getSimpleName());

        return entryPoint;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
