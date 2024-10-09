package io.github.zhyshko.core;

import io.github.zhyshko.core.bot.RequestHandlerBot;
import io.github.zhyshko.core.configuration.ConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RequestHandlerBot.class)
@EnableConfigurationProperties(ConfigProperties.class)
@ComponentScan(basePackages = {"io.github.zhyshko"})
public class BotIoApplicationAutoConfiguration {

}

