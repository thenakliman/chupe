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
@Entity(name = "best_practices_assessment_answer")
@Table
public class PracticeAssessmentAnswer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "best_practice_id", referencedColumnName = "id")
  private BestPractice bestPractice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "best_practice_assessment_id", referencedColumnName = "id")
  private PracticeAssessment practicesAssessment;

  @Column(name = "answer", nullable = false)
  private Boolean answer;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
