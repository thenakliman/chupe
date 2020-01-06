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
@Entity(name = "retrospection_action_item")
@Table
public class RetroActionItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retro_id", referencedColumnName = "id")
  private Retro retro;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retro_point_id", referencedColumnName = "id")
  private RetroPoint retroPoint;

  @Column(name = "description", nullable = false, length = 2000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 256)
  private ActionItemStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", referencedColumnName = "username")
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assigned_to", referencedColumnName = "username")
  private User assignedTo;

  @Column(name = "last_date_to_act", nullable = false)
  private Date deadlineToAct;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
