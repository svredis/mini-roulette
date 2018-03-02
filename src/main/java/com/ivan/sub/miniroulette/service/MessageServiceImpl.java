package com.ivan.sub.miniroulette.service;

import static java.lang.String.format;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.ivan.sub.miniroulette.model.Round;
import com.ivan.sub.miniroulette.model.Session;

/**
 * Created on 2/27/18.
 */
@Service
public class MessageServiceImpl implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

  private static final String BALANCE_CHANGED_EVENT_NAME = "balanceChanged";

  private ConcurrentMap<String, SseEmitter> emittersBySessionId = new ConcurrentHashMap<>();

  @Override
  public SseEmitter getEmitter(String sessionId) {
    SseEmitter emitter = emittersBySessionId.get(sessionId);
    if (emitter == null) {
      logger.debug("Adding emitter for session with id = {}", sessionId);
      emitter = new SseEmitter(180_000L);
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

  @Override
  public void sendMessage(String sessionId, String msg) {
    try {
      SseEmitter emitter = emittersBySessionId.get(sessionId);
      if (emitter != null) {
        emitter.send(msg);
      }
    } catch (IOException e) {
      logger.error("Error during sending message by emitter of session with id = {}", sessionId);
    }
  }

  @Override
  public void sendMessage(String sessionId, String msg, Integer balance) {
    try {
      SseEmitter emitter = emittersBySessionId.get(sessionId);
      if (emitter != null) {
        SseEventBuilder builder = SseEmitter.event()
            .data(new BalanceChangedResponse(msg, balance))
            .name(BALANCE_CHANGED_EVENT_NAME);
        emitter.send(builder);
      }
    } catch (IOException e) {
      logger.error("Error during sending msg by emitter of session with id = {}", sessionId);
    }
  }

  @Override
  public void sendMessage(Session session, String msg) {
    logger.debug("Sending message [ {} ] for session {}", msg, session.getSessionId());
    try {
      SseEmitter emitter = emittersBySessionId.get(session.getSessionId());
      if (emitter != null) {
        SseEventBuilder builder = SseEmitter.event()
            .data(new BalanceChangedResponse(msg, session.getBalance()))
            .name(BALANCE_CHANGED_EVENT_NAME);
        emitter.send(builder);
      }
    } catch (IOException e) {
      logger.error("Error during sending msg by emitter of session with id = {}", session.getSessionId());
    }
  }

  @Override
  public void sendRoundResult(Round round) {
    String msg = round.getResult() == 1
        ? format("You won in round %d. Your balance is increased by 2 units.", round.getNumber())
        : format("You lost in round %d.", round.getNumber());
    round.getBids().forEach(bid -> sendMessage(bid.getSession(), msg));
  }

  private class BalanceChangedResponse {
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
