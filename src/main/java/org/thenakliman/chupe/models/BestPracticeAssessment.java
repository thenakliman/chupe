package org.thenakliman.chupe.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "best_practice_assessment")
@Table
public class BestPracticeAssessment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retrospection_id", referencedColumnName = "id")
  private Retro retro;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "answered_by", referencedColumnName = "username")
  private User answeredBy;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "bestPracticeAssessment")
  private List<BestPracticeAssessmentAnswer> bestPracticeAssessmentAnswers;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}