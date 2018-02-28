package com.ivan.sub.miniroulette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on 2/28/18.
 */
@Entity
@Table(name = "session")
public class Session extends BaseEntity {

  @Column(name = "session_id", unique = true, nullable = false, updatable = false, length = 61)
  private String sessionId;

  @Column(name = "balance", nullable = false)
  private Integer balance;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }
}
