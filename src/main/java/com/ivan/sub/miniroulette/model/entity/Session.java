package com.ivan.sub.miniroulette.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Session representation.
 */
@Entity
@Table(name = "session")
public class Session {

  @Id
  private String id;

  @Column(name = "balance", nullable = false)
  private Integer balance;

  @Column(name = "is_in_game", nullable = false)
  private Boolean isInGame;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  public Boolean getIsInGame() {
    return isInGame;
  }

  public void setIsInGame(Boolean inGame) {
    isInGame = inGame;
  }

  @Override
  public String toString() {
    return "Session{" +
        " balance=" + balance +
        ", isInGame=" + isInGame +
        '}';
  }
}
