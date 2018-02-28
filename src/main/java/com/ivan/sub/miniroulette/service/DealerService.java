package com.ivan.sub.miniroulette.service;

import com.ivan.sub.miniroulette.model.Session;

/**
 * Created on 2/28/18.
 */
public interface DealerService {

  Session takeBet(String sessionId);

}
