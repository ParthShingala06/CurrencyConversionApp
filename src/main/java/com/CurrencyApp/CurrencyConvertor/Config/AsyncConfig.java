package com.CurrencyApp.CurrencyConvertor.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "currencyExchangeExecutor")
    public Executor currencyExchangeExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Minimum number of threads
        executor.setMaxPoolSize(20); // Maximum number of threads
        executor.setQueueCapacity(100); // Queue capacity before rejecting new tasks
        executor.setThreadNamePrefix("CurrencyThread-"); // Thread name prefix
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
