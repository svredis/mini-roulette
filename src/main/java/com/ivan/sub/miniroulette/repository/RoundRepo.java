package com.ivan.sub.miniroulette.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ivan.sub.miniroulette.model.entity.Round;

/**
 * Spring data repository for {@link Round} entity.
 *
 * @see CrudRepository
 */
@Repository
public interface RoundRepo extends CrudRepository<Round, Long> {
}
