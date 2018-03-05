package com.ivan.sub.miniroulette.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ivan.sub.miniroulette.model.entity.Session;

/**
 * Service to manage {@link SseEmitter} for sessions.
 */
public interface EmitterService {

  /**
   * Get emitter which was associated with given session or create new one if no emitters found for given session.
   *
   * @param sessionId is http session id for which need return emitter.
   *
   * @return emitter assiciated with given session.
   */
  SseEmitter getEmitter(String sessionId);

  /**
   * Send message to given session using {@link SseEmitter}.
   *
   * @param session is session for which need to send a message.
   * @param msg is message for session.
   */
  void sendMessage(Session session, String msg);

}
