package com.ivan.sub.miniroulette.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivan.sub.miniroulette.model.Session;

/**
 * Created on 2/28/18.
 */
@Repository
public interface SessionRepo extends CrudRepository<Session, Long> {

  Session findBySessionId(String sessionId);

}
