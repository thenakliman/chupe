package org.thenakliman.chupe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "fund")
@Table
public class Fund {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "type", referencedColumnName = "id")
  private FundType type;

  @Column(name = "amount", nullable = false)
  private long amount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "username")
  private User owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "addedBy", referencedColumnName = "username")
  private User addedBy;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)
  private TransactionType transactionType;

  @Column(name = "is_approved")
  private boolean isApproved;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}