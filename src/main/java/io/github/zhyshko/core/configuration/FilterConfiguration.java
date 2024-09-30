package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.filter.FilterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterConfiguration {

    private List<String> filters;

    private final ApplicationContext applicationContext;

    @Autowired
    public FilterConfiguration(ConfigProperties config, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if(config.getFilters() != null) {
            this.filters = config.getFilters();
        }
    }

    @Bean("filters")
    public List<FilterAdapter> getFilters() {
        if(this.filters == null) {
            return List.of();
        }
        return filters.stream()
                .map(s -> applicationContext.getBean(s, FilterAdapter.class))
                .toList();

    }
}
