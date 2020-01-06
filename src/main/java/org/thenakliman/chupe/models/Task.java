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
@Entity(name = "task")
@Table
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private TaskState state = TaskState.CREATED;

  @Column(name = "progress", nullable = false)
  private int progress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", referencedColumnName = "username")
  private User createdBy;

  @Column(name = "started_on")
  private Date startOn;

  @Column(name = "ended_on")
  private Date endedOn;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
