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
@Entity(name = "retrospection_point")
@Table
public class RetroPoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retro_id", referencedColumnName = "id")
  private Retro retro;

  @Column(name = "description", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private RetroPointType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "added_by", referencedColumnName = "username")
  private User addedBy;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
