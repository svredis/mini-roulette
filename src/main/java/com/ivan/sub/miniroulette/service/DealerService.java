package com.ivan.sub.miniroulette.service;

/**
 * Service to manage roulette game: spin roulette and handle bids.
 */
public interface DealerService {

  /**
   * Register session with given id to next round if session hasn't been already registered or session has enough units to play a game.
   *
   * @param sessionId is session id to take bid from it.
   *
   * @throws IllegalStateException if session with given id wasn't initialized.
   */
  void takeBid(String sessionId);

  /**
   * Generate random round result and give units to registered sessions if they win.
   */
  void spin();

}
