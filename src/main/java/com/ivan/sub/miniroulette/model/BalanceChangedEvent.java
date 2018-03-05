package com.ivan.sub.miniroulette.model;

import static java.util.Collections.singletonList;

import java.util.List;

import com.ivan.sub.miniroulette.model.entity.Session;
import com.ivan.sub.miniroulette.service.MessageListener;

/**
 * Event to use it when balance was changed for one or more sessions. This event has handler in {@link MessageListener}.
 */
public class BalanceChangedEvent {

  /**
   * Message to send it for each session from {@code sessions} list.
   */
  private String message;

  /**
   * List of sessions for which need to send a message.
   */
  private List<Session> sessions;

  public BalanceChangedEvent(List<Session> sessions, String message) {
    this.sessions = sessions;
    this.message = message;
  }

  public BalanceChangedEvent(Session session, String message) {
    this.sessions = singletonList(session);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Session> getSessions() {
    return sessions;
  }

  public void setSessions(List<Session> sessions) {
    this.sessions = sessions;
  }
}
