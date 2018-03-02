package com.ivan.sub.miniroulette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@PropertySource("classpath:game.properties")
@EnableScheduling
@EnableTransactionManagement
public class MiniRouletteApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiniRouletteApplication.class, args);
  }
}
