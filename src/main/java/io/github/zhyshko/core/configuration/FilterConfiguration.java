package io.github.zhyshko.core.configuration;

import io.github.zhyshko.core.filter.FilterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterConfiguration {


    @Value("${filters}")
    private List<String> filters;

    private ApplicationContext applicationContext;

    @Bean
    public List<FilterAdapter> prepareFilters() {
        return filters.stream()
                .map(s -> applicationContext.getBean(s, FilterAdapter.class))
                .toList();

    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
