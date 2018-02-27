package com.ivan.sub.miniroulette.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Created on 2/27/18.
 */
@Service
public class MessageServiceImpl implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

  private ConcurrentMap<String, SseEmitter> emittersBySessionId = new ConcurrentHashMap<>();

  @Override
  public SseEmitter getEmitter(String sessionId) {
    SseEmitter emitter = emittersBySessionId.get(sessionId);
    if (emitter == null) {
      logger.debug("Adding emitter for session with id = {}", sessionId);
      emitter = new SseEmitter();
      emitter.onCompletion(() -> {
        logger.debug("Emitter onCompletion event. Remove emitter from map with session id = {}", sessionId);
        this.emittersBySessionId.remove(sessionId);
      });
      emitter.onTimeout(() -> {
        logger.debug("Emitter onTimeout event. Remove emitter from map with session id = {}", sessionId);
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
      logger.error("Error during sending msg by emitter of session with id = {}", sessionId);
    }
  }
}
