package io.github.zhyshko.core.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "botio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigProperties {

    private String botToken;
    private List<String> filters;
    private boolean i18nEnabled;
    private boolean multiThreadedUpdateConsumerEnabled;
    private boolean perUserRequestLockingEnabled;

}
