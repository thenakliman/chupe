package org.thenakliman.chupe.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "best_practices_assessment_answer")
@Table
public class BestPracticeAssessmentAnswer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "best_practice_id", referencedColumnName = "id")
  private BestPractice bestPractice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "best_practice_assessment_id", referencedColumnName = "id")
  private BestPracticeAssessment bestPracticeAssessment;

  @Column(name = "answer", nullable = false)
  private Boolean answer;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
