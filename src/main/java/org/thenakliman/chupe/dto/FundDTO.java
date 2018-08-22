package org.thenakliman.chupe.dto;

import org.thenakliman.chupe.models.TransactionType;

public class FundDTO {
  private long id;

  private long type;

  private long amount;

  private String owner;

  private String addedBy;

  private TransactionType transactionType;

  private boolean isApproved;

  /** Empty constructor for spring to create bean. */
  public FundDTO() {
    /** For spring to create empty object */
  }

  /** Constructor for FundDto. */
  public FundDTO(long id,
                 long type,
                 long amount,
                 String owner,
                 String addedBy,
                 TransactionType transactionType,
                 boolean isApproved) {
    this.id = id;
    this.type = type;
    this.addedBy = addedBy;
    this.amount = amount;
    this.owner = owner;
    this.transactionType = transactionType;
    this.isApproved = isApproved;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public long getType() {
    return type;
  }

  public void setType(long type) {
    this.type = type;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public boolean isApproved() {
    return isApproved;
  }

  public void setApproved(boolean approved) {
    isApproved = approved;
  }

  public String getAddedBy() {
    return addedBy;
  }

  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
