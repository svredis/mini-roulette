package com.ivan.sub.miniroulette.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivan.sub.miniroulette.model.BalanceChangedEvent;
import com.ivan.sub.miniroulette.model.entity.Round;
import com.ivan.sub.miniroulette.model.entity.Session;
import com.ivan.sub.miniroulette.repository.RoundRepo;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Implementation of {@link DealerService}.
 */
@Service
public class DealerServiceImpl implements DealerService {

  private static final Logger logger = LoggerFactory.getLogger(DealerServiceImpl.class);

  @Value("${round.won.increment}")
  private int wonIncrement;
  @Value("${round.lost.increment}")
  private int lostIncrement;
  @Value("${message.round.won}")
  private String wonMessage;
  @Value("${message.round.lost}")
  private String lostMessage;
  @Value("${message.bid.was.accepted}")
  private String bidWasAcceptedMessage;

  private ApplicationEventPublisher eventPublisher;

  private SessionRepo sessionRepo;

  private RoundRepo roundRepo;

  private Random random = new Random();

  @Autowired
  public DealerServiceImpl(ApplicationEventPublisher eventPublisher, SessionRepo sessionRepo, RoundRepo roundRepo) {
    this.eventPublisher = eventPublisher;
    this.sessionRepo = sessionRepo;
    this.roundRepo = roundRepo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void spin() {
    logger.debug("Start spin.");

    List<String> playersIds = sessionRepo.findByIsInGameIsTrue();

    if (playersIds.size() > 0) {
      Round newRound = roundRepo.save(new Round((byte) random.nextInt(2)));
      sessionRepo.updateWithRoundResult(playersIds, newRound.getResult() == 1 ? wonIncrement : lostIncrement);

      List<Session> players = sessionRepo.findByIdIn(playersIds);
      eventPublisher.publishEvent(
          new BalanceChangedEvent(players, newRound.getResult() == 1 ? format(wonMessage, newRound.getResult()) : format(lostMessage, newRound.getResult()))
      );

      logger.debug("End spin for round {} with result = {}. Players size = {}.", newRound.getNumber(), newRound.getResult(), players.size());
    }

    logger.debug("End spin. There are no registered players.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void takeBid(String sessionId) {
    logger.debug("Start taking a bid from session {}.", sessionId);

    int updateResult = sessionRepo.updateToBeInGame(sessionId);

    Session session = sessionRepo.findOne(sessionId);

    if (session == null) {
      throw new IllegalStateException("Session wasn't initialized.");
    }

    if (updateResult == 1) {
      eventPublisher.publishEvent(new BalanceChangedEvent(session, bidWasAcceptedMessage));
    }

    logger.debug("Finished taking a bid from session {}.", sessionId);
  }

}
