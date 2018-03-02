package com.ivan.sub.miniroulette.service;

import com.ivan.sub.miniroulette.model.Round;
import com.ivan.sub.miniroulette.model.Session;

/**
 * Created on 2/28/18.
 */
public interface DealerService {

  Session takeBid(String sessionId);

  Round spin(boolean createNewRound);

  void prepareNewRound();

}
