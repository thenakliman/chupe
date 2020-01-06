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
@Entity(name = "answers")
@Table
public class Answer {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "answer", nullable = false)
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "answered_by", referencedColumnName = "username")
  private User answeredBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", referencedColumnName = "id")
  private Question question;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;
}
