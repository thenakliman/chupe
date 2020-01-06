package org.thenakliman.chupe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "fundType")
@Table(name = "fund_type")
public class FundType {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "default_amount")
  private int defaultAmount;

  @Column(name = "type", unique = true, nullable = false)
  private String type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "createdBy", referencedColumnName = "username")
  private User createdBy;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}

