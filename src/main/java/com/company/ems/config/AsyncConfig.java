package com.company.ems.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("smsTaskExecutor")
    public Executor smsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：同时执行的线程数
        executor.setCorePoolSize(5);

        // 最大线程数：线程池最大容量
        executor.setMaxPoolSize(10);

        // 队列容量：等待队列的大小
        executor.setQueueCapacity(100);

        // 线程存活时间：空闲线程的存活时间
        executor.setKeepAliveSeconds(60);

        // 线程名称前缀
        executor.setThreadNamePrefix("SMS-");

        // 拒绝策略：当线程池和队列都满时的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;
    }
}
