package com.ivan.sub.miniroulette.service;

import static java.lang.Boolean.FALSE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivan.sub.miniroulette.model.BalanceChangedEvent;
import com.ivan.sub.miniroulette.model.entity.Session;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Created on 2/28/18.
 */
@Service
public class SessionServiceImpl implements SessionService {

  @Value("${session.start.balance}")
  private Integer startBalance;
  @Value("${message.balance.initialized}")
  private String balanceInitializedMessage;

  private ApplicationEventPublisher eventPublisher;

  private SessionRepo sessionRepo;

  @Autowired
  public SessionServiceImpl(ApplicationEventPublisher eventPublisher, SessionRepo sessionRepo) {
    this.eventPublisher = eventPublisher;
    this.sessionRepo = sessionRepo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void checkSessionWasInitialized(String sessionId) {
    Session session = sessionRepo.findOne(sessionId);
    if (session == null) {
      session = new Session();
      session.setId(sessionId);
      session.setBalance(startBalance);
      session.setIsInGame(FALSE);
      Session newSession = sessionRepo.save(session);
      eventPublisher.publishEvent(new BalanceChangedEvent(newSession, balanceInitializedMessage));
    }
  }

}
