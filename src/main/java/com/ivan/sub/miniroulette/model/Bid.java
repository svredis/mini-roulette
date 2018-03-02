package com.ivan.sub.miniroulette.model;

import static org.hibernate.annotations.CascadeType.PERSIST;
import static org.hibernate.annotations.CascadeType.REFRESH;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * Created on 2/28/18.
 */
@Entity
@Table(name = "bid")
public class Bid extends BaseEntity {

  @Column(name = "is_played", nullable = false)
  private Boolean isPlayed;

  @JoinColumn(name = "session_id", nullable = false, updatable = false)
  @ManyToOne
  @Cascade({PERSIST, REFRESH, SAVE_UPDATE})
  private Session session;

  @JoinColumn(name = "round_id", nullable = false, updatable = false)
  @ManyToOne
  @Cascade({PERSIST, REFRESH, SAVE_UPDATE})
  private Round round;

  public Boolean getPlayed() {
    return isPlayed;
  }

  public void setPlayed(Boolean played) {
    isPlayed = played;
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public Round getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round;
  }
}
