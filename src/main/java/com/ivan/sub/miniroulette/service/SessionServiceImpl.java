package com.ivan.sub.miniroulette.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivan.sub.miniroulette.model.Session;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Created on 2/28/18.
 */
@Service
public class SessionServiceImpl implements SessionService {

  @Value("${session.start.balance}")
  private Integer startBalance;

  private SessionRepo sessionRepo;

  @Autowired
  public SessionServiceImpl(SessionRepo sessionRepo) {
    this.sessionRepo = sessionRepo;
  }

  @Override
  @Transactional
  public Session getSession(String sessionId) {
    Session session = sessionRepo.findBySessionId(sessionId);
    if (session == null) {
      session = new Session();
      session.setSessionId(sessionId);
      session.setBalance(startBalance);
      session = sessionRepo.save(session);
    }
    return session;
  }

}
