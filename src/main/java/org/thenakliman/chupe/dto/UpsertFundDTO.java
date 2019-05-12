package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.TransactionType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpsertFundDTO {
  private long type;
  private long amount;
  private String owner;
  private TransactionType transactionType;
}
