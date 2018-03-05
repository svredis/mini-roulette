package com.ivan.sub.miniroulette.service;

/**
 * Service to manage roulette game. It can spin roulette to get some result of round and it can take bids from sessions.
 */
public interface DealerService {

  /**
   * Register session with given id to next round if session hasn't been already registered or session has enough units to play a game.
   *
   * @param sessionId is session id to take bid from it.
   */
  void takeBid(String sessionId);

  /**
   * Generate round result and give units to registered sessions if they win.
   */
  void spin();

}
