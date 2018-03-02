package com.ivan.sub.miniroulette.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivan.sub.miniroulette.model.Bid;

/**
 * Created on 2/28/18.
 */
@Repository
public interface BidRepo extends CrudRepository<Bid, Long> {

  Integer countBidBySessionIdIsAndRoundIdIs(Long sessionId, Long roundId);

  Stream<Bid> findByRoundId(Long roundId);

  List<Bid> save(List<Bid> bids);

}
