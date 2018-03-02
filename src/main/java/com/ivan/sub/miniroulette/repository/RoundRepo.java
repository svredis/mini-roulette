package com.ivan.sub.miniroulette.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivan.sub.miniroulette.model.Round;

/**
 * Created on 2/28/18.
 */
@Repository
public interface RoundRepo extends CrudRepository<Round, Long> {

  Round findByResultIsNull();

  @Query("SELECT round FROM Round round WHERE round.result IS NULL")
  @EntityGraph(attributePaths = "bids")
  Round findByResultIsNullFetchBids();

  @Query("SELECT coalesce(max(round.number), 0) FROM Round round")
  Long getMaxRoundNumber();
}
