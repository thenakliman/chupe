package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.TransactionType;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FundDTO {
  private long id;

  private long type;

  private long amount;

  private String owner;

  private String addedBy;

  private TransactionType transactionType;

  private boolean approved;

  private Date createdAt;
}
