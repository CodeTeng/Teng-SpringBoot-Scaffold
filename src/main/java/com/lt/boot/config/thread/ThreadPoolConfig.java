package com.lt.boot.config.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/19 19:17
 */
@Configuration
public class ThreadPoolConfig {
    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    @Bean
    public Executor handleSysLogTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        // 最大可创建的线程数
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        // 等待队列最大长度
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        // 线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        // 线程名称
        executor.setThreadNamePrefix("syslog-task-handler-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
