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
@Entity(name = "questions")
@Table
public class Question {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "question", nullable = false)
  private String question;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "username")
  private User owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assigned_to", referencedColumnName = "username")
  private User assignedTo;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private QuestionStatus status = QuestionStatus.OPEN;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false)
  private QuestionPriority priority;
}
