package com.ivan.sub.miniroulette.model;

import static org.hibernate.annotations.FetchMode.SUBSELECT;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Created on 2/28/18.
 */
@Entity
@Table(name = "round")
public class Round extends BaseEntity {

  @Column(name = "number")
  private Long number;

  @Column(name = "result")
  private Byte result;

  @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
  @Fetch(SUBSELECT)
  private List<Bid> bids;

  public Round() {
  }

  public Round(Long number) {
    this.number = number;
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

  public List<Bid> getBids() {
    return bids;
  }

  public void setBids(List<Bid> bids) {
    this.bids = bids;
  }
}
