package com.ivan.sub.miniroulette.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ivan.sub.miniroulette.model.entity.Session;

/**
 * Spring data repository for {@link Session} entity.
 *
 * @see CrudRepository
 */
@Repository
public interface SessionRepo extends CrudRepository<Session, String> {

  /**
   * Find all sessions which were registered for nearest round.
   *
   * @return list of found sessions ids.
   */
  @Query("SELECT s.id FROM Session s WHERE s.isInGame = TRUE ")
  List<String> findByIsInGameIsTrue();

  /**
   * Update all sessions with given ids. For each session it increases balance for given increment and sets {@code is_in_game} flag to {@code false}.
   *
   * @param ids is sessions ids for which need to set round result.
   * @param increment is amount of units to increase balance for each given session.
   */
  @Modifying
  @Query(value = "UPDATE session s SET s.balance = s.balance + :increment, s.is_in_game = FALSE WHERE s.id IN :ids", nativeQuery = true)
  void updateWithRoundResult(@Param("ids") List<String> ids, @Param("increment") Integer increment);

  List<Session> findByIdIn(List<String> ids);

  /**
   * Update session with given id. It sets flag {@code is_in_game} to true and decreases balance for 1 unit.
   *
   * @param id is session id to update it session.
   *
   * @return 1 if session was updated and 0 if session wasn't updated.
   */
  @Modifying
  @Query(value = "UPDATE session s SET s.balance = s.balance - 1, s.is_in_game = TRUE WHERE s.id = :id AND s.is_in_game = FALSE AND s.balance > 0",
      nativeQuery = true)
  int updateToBeInGame(@Param("id") String id);

}
