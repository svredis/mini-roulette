package com.ivan.sub.miniroulette.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivan.sub.miniroulette.model.Session;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Created on 2/28/18.
 */
@Service
public class DealerServiceImpl implements DealerService {

  private SessionRepo sessionRepo;

  @Autowired
  public DealerServiceImpl(SessionRepo sessionRepo) {
    this.sessionRepo = sessionRepo;
  }

  @Override
  @Transactional
  public Session takeBet(String sessionId) {
    Session session = sessionRepo.findBySessionId(sessionId);
    if (session == null) {
      throw new IllegalStateException("Session wasn't initialized.");
    }
    if (session.getBalance() < 1) {
      throw new IllegalStateException("You don't have enough units to do a bet.");
    }

    session.setBalance(session.getBalance() - 1);
    session = sessionRepo.save(session);
    return session;
  }
}
