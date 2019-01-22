package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.thenakliman.chupe.models.TransactionType;

@Builder
@AllArgsConstructor
@Data
public class TeamMemberFund {
  private long totalAmount;

  private String owner;

  private TransactionType transactionType;

  private boolean isApproved;
}