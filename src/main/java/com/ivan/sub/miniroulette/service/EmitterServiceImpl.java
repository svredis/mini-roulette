package com.ivan.sub.miniroulette.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.ivan.sub.miniroulette.model.entity.Session;

/**
 * Implementation of {@link EmitterService}.
 */
@Service
public class EmitterServiceImpl implements EmitterService {

  private static final Logger logger = LoggerFactory.getLogger(EmitterServiceImpl.class);

  private static final String BALANCE_CHANGED_EVENT_NAME = "balanceChanged";

  private ConcurrentMap<String, SseEmitter> emittersBySessionId = new ConcurrentHashMap<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public SseEmitter getEmitter(String sessionId) {
    SseEmitter emitter = emittersBySessionId.get(sessionId);
    if (emitter == null) {
      logger.debug("Adding emitter for session with id = {}", sessionId);
      emitter = new SseEmitter();
      emitter.onCompletion(() -> {
        logger.debug("Emitter completed. Removing emitter from map for session with id = {}", sessionId);
        this.emittersBySessionId.remove(sessionId);
      });
      emitter.onTimeout(() -> {
        logger.debug("Emitter timeout. Removing emitter from map for session with id = {}", sessionId);
        this.emittersBySessionId.remove(sessionId);
      });
      emittersBySessionId.putIfAbsent(sessionId, emitter);
      emitter = emittersBySessionId.get(sessionId);
    }
    return emitter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(Session session, String msg) {
    SseEmitter emitter = emittersBySessionId.get(session.getId());
    logger.debug("Sending message [{}] for session [{}] with balance = {} using emitter {}.", msg, session.getId(), session.getBalance(), emitter);
    if (emitter != null) {
      try {
        SseEventBuilder builder = SseEmitter.event()
            .data(new BalanceChangedResponse(msg, session.getBalance()))
            .name(BALANCE_CHANGED_EVENT_NAME);
        emitter.send(builder);
      } catch (IOException e) {
        emittersBySessionId.remove(session.getId());
        logger.error("Error during sending msg [{}] for session with id = {}.", msg, session.getId());
      }
    }

  }

  private static class BalanceChangedResponse {
    private String message;
    private Integer balance;

    BalanceChangedResponse(String message, Integer balance) {
      this.message = message;
      this.balance = balance;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Integer getBalance() {
      return balance;
    }

    public void setBalance(Integer balance) {
      this.balance = balance;
    }
  }

}
