package io.github.zhyshko.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class LoggingRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        LOG.error("Thread pool is saturated, update is dismissed.");
    }
}
