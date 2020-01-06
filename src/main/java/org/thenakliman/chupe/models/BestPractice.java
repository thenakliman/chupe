package org.thenakliman.chupe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "best_practice")
@Table
public class BestPractice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "applicable", nullable = false)
  private Boolean applicable;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "createdBy", referencedColumnName = "username")
  private User createdBy;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
