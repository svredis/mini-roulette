package com.ivan.sub.miniroulette.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Scheduled job that start playing round in the game each defined period.
 */
@Service
public class GameEngine {

  private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

  private DealerService dealerService;

  @Autowired
  public GameEngine(DealerService dealerService) {
    this.dealerService = dealerService;
  }

  @Scheduled(fixedRate = 1000)
  public void playRound() {
    logger.debug("Starting new round.");
    dealerService.spin();
  }
}
