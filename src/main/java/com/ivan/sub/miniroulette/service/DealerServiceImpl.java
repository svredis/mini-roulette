package com.ivan.sub.miniroulette.service;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ivan.sub.miniroulette.model.Bid;
import com.ivan.sub.miniroulette.model.Round;
import com.ivan.sub.miniroulette.model.Session;
import com.ivan.sub.miniroulette.repository.BidRepo;
import com.ivan.sub.miniroulette.repository.RoundRepo;
import com.ivan.sub.miniroulette.repository.SessionRepo;

/**
 * Created on 2/28/18.
 */
@Service
public class DealerServiceImpl implements DealerService {

  private static final Logger logger = LoggerFactory.getLogger(DealerServiceImpl.class);

  private SessionRepo sessionRepo;

  private RoundRepo roundRepo;

  private BidRepo bidRepo;

  private Random random = new Random();

  @Autowired
  public DealerServiceImpl(SessionRepo sessionRepo, RoundRepo roundRepo, BidRepo bidRepo) {
    this.sessionRepo = sessionRepo;
    this.roundRepo = roundRepo;
    this.bidRepo = bidRepo;
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Session takeBid(String sessionId) {
    Session session = sessionRepo.findBySessionId(sessionId);
    if (session == null) {
      throw new IllegalStateException("Session wasn't initialized.");
    }
    if (session.getBalance() < 1) {
      throw new IllegalStateException("You don't have enough units to do a bet.");
    }

    session.setBalance(session.getBalance() - 1);
    session = sessionRepo.save(session);

    createNewBid(session);

    return session;
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Round spin(boolean createNewRound) {
    Byte result = (byte) random.nextInt(2);
    Round playedRound = roundRepo.findByResultIsNullFetchBids();
    playedRound.setResult(result);
    playedRound.getBids().forEach(bid -> {
      bid.setPlayed(TRUE);
      bid.getSession().setBalance(bid.getSession().getBalance() + (result == 1 ? 2 : 0));
    });
    roundRepo.save(playedRound);
    if (createNewRound) {
      prepareNewRound();
    }
    return playedRound;
  }


  @Override
  public void prepareNewRound() {
    roundRepo.save(new Round(roundRepo.getMaxRoundNumber() + 1));
  }


  private void createNewBid(Session session) {
    Round notPlayedRound = roundRepo.findByResultIsNull();

    if (bidRepo.countBidBySessionIdIsAndRoundIdIs(session.getId(), notPlayedRound.getId()) > 0) {
      throw new IllegalStateException("The bid already accepted.");
    }

    Bid bid = new Bid();
    bid.setSession(session);
    bid.setPlayed(FALSE);
    bid.setRound(notPlayedRound);
    bidRepo.save(bid);

  }
}
