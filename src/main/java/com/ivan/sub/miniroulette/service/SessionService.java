package com.ivan.sub.miniroulette.service;

import com.ivan.sub.miniroulette.model.entity.Session;

/**
 * Service to manage {@link Session} entities.
 */
public interface SessionService {

  /**
   * Check if session with given id is exist in database. If there is no session in database it creates new one.
   *
   * @param sessionId is http session id to check if there is session in db with given id.
   */
  void checkSessionWasInitialized(String sessionId);

}
