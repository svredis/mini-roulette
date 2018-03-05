package com.ivan.sub.miniroulette;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ivan.sub.miniroulette.service.MessageListener;

/**
 * Main configuration class for application.
 */
@SpringBootApplication
@PropertySource("classpath:roulette.properties")
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class MiniRouletteApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiniRouletteApplication.class, args);
  }

  /**
   * Executor to start async events handling in {@link MessageListener}.
   *
   * @param env is environment of this application.
   *
   * @return configured {@link Executor}.
   */
  @Bean
  @Qualifier("messageEventExecutor")
  public Executor asyncExecutor(Environment env) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Integer.valueOf(env.getProperty("executor.core.pool.size")));
    executor.setMaxPoolSize(Integer.valueOf(env.getProperty("executor.max.pool.size")));
    executor.setQueueCapacity(Integer.valueOf(env.getProperty("executor.queue.capacity")));
    executor.setThreadNamePrefix("message-listener-");
    executor.initialize();
    return executor;
  }
}
