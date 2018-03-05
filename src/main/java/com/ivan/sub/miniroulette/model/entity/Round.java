package com.ivan.sub.miniroulette.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Round representation.
 */
@Entity
@Table(name = "round")
public class Round {

  @Id
  @Column(name = "number")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long number;

  @Column(name = "result")
  private Byte result;

  public Round() {
  }

  public Round(Byte result) {
    this.result = result;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public Byte getResult() {
    return result;
  }

  public void setResult(Byte result) {
    this.result = result;
  }

}
