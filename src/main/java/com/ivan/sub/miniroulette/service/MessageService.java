package com.ivan.sub.miniroulette.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Created on 2/27/18.
 */
public interface MessageService {

  SseEmitter getEmitter(String sessionId);

  void sendMessage(String sessionId, String msg);

  void sendMessage(String sessionId, String msg, Integer balance);

}
