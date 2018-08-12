package org.thenakliman.chupe.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "fundType")
@Table(name = "fund_type")
public class FundType {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "default_amount")
  private int defaultAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", unique = true)
  private FundTypes type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "createdBy", referencedColumnName = "username")
  private User createdBy;

  @Column(name = "description")
  private String description;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getDefaultAmount() {
    return defaultAmount;
  }

  public void setDefaultAmount(int defaultAmount) {
    this.defaultAmount = defaultAmount;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public FundTypes getType() {
    return type;
  }

  public void setType(FundTypes type) {
    this.type = type;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

