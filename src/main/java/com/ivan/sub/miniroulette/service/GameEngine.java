package com.ivan.sub.miniroulette.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ivan.sub.miniroulette.model.Round;

/**
 * Created on 2/28/18.
 */
@Service
public class GameEngine {

  private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

  private DealerService dealerService;

  private MessageService messageService;

  @Autowired
  public GameEngine(DealerService dealerService, MessageService messageService) {
    this.dealerService = dealerService;
    this.messageService = messageService;
  }

  @Scheduled(fixedRate = 1000)
  public void playRound() {
    logger.debug("Starting new round.");
    Round round = dealerService.spin(true);
    messageService.sendRoundResult(round);
    logger.debug("Round {} finished with result = {}", round.getNumber(), round.getResult());
  }

  @PostConstruct
  public void prepareFirstRound() {
    dealerService.prepareNewRound();
  }

  @PreDestroy
  public void playLastRound() {
    logger.debug("Starting last round.");
    Round round = dealerService.spin(false);
    messageService.sendRoundResult(round);
    logger.debug("Round {} finished with result = {}", round.getNumber(), round.getResult());

  }
}
