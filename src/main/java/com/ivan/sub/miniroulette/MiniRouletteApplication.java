package com.ivan.sub.miniroulette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:game.properties")
public class MiniRouletteApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiniRouletteApplication.class, args);
  }
}
