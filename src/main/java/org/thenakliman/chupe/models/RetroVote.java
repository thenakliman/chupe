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
@Entity(name = "retrospection_vote")
@Table
public class RetroVote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voted_by", referencedColumnName = "username")
  private User votedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retro_point_id", referencedColumnName = "id")
  private RetroPoint retroPoint;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
