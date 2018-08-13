package org.thenakliman.chupe.dto;

import org.thenakliman.chupe.models.TransactionType;

public class TeamMemberFund {
  private long totalAmount;

  private String owner;

  private TransactionType transactionType;

  private boolean isApproved;

  /** Constructor for TeamMemberFund.
   *
   * @param totalAmount to be payed or already payed
   * @param owner of the total amount
   * @param transactionType is DEBIT or CREDIT
   * @param isApproved shows whether amount is approved or not
   */
  public TeamMemberFund(long totalAmount,
                        String owner,
                        TransactionType transactionType,
                        boolean isApproved) {
    this.totalAmount = totalAmount;
    this.owner = owner;
    this.transactionType = transactionType;
    this.isApproved = isApproved;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public long getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(long totalAmount) {
    this.totalAmount = totalAmount;
  }

  public boolean isApproved() {
    return isApproved;
  }

  public void setApproved(boolean approved) {
    isApproved = approved;
  }
}