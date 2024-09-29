package io.github.zhyshko.core.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "botio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {


    private String botToken;
    private List<String> filters;

}
