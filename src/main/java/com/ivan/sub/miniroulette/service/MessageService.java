package com.ivan.sub.miniroulette.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ivan.sub.miniroulette.model.Round;
import com.ivan.sub.miniroulette.model.Session;

/**
 * Created on 2/27/18.
 */
public interface MessageService {

  SseEmitter getEmitter(String sessionId);

  void sendMessage(String sessionId, String msg);

  void sendMessage(String sessionId, String msg, Integer balance);

  void sendRoundResult(Round round);

  void sendMessage(Session session, String msg);

}
