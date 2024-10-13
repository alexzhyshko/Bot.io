package io.github.zhyshko.core.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Order
public class DefaultsEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DEFAULT_THREADPOOL_PROPERTIES = "defaultThreadPoolProperties";
    private static final String DEFAULT_THREADPOOL_PROPERTIES_LOCATION = "/config/default.yaml";

    private final YamlPropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource myFrameworkDefaults = new ClassPathResource(DEFAULT_THREADPOOL_PROPERTIES_LOCATION);
        try {
            propertySourceLoader.load(DEFAULT_THREADPOOL_PROPERTIES, myFrameworkDefaults)
                    .forEach(s -> environment.getPropertySources().addLast(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
