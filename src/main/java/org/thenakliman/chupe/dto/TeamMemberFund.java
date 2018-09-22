package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.thenakliman.chupe.models.TransactionType;

@AllArgsConstructor
@Data
public class TeamMemberFund {
  private long totalAmount;

  private String owner;

  private TransactionType transactionType;

  private boolean isApproved;
}