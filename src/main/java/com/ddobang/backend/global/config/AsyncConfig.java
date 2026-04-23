package com.ddobang.backend.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig
 * 비동기처리에 관련된 설정
 * @author 100minha
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "statEventExecutor")
	public Executor statEventExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(8);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("stat-event-");
		executor.initialize();

		return executor;
	}

	@Bean(name = "s3EventExecutor")
	public Executor s3EventExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(8);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("s3-event-");
		executor.initialize();

		return executor;
	}
}
