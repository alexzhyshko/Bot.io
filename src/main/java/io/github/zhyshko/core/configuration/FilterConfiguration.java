package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.filter.FilterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(FilterConfiguration.class);

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
        List<FilterAdapter> filterBeans = filters.stream()
                .map(s -> applicationContext.getBean(s, FilterAdapter.class))
                .toList();

        if(!filterBeans.isEmpty()) {
            LOG.info("Found and initialized {} filters", filterBeans.size());
            if(LOG.isDebugEnabled()) {
                LOG.debug("Loaded filters in the following order: {}", filterBeans);
            }
        }

        return filterBeans;
    }
}
