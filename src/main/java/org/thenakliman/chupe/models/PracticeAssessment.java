package org.thenakliman.chupe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "best_practice_assessment")
@Table
public class PracticeAssessment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "retrospection_id", referencedColumnName = "id")
  private Retro retro;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "answered_by", referencedColumnName = "username")
  private User answeredBy;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "practicesAssessment", cascade = CascadeType.ALL)
  private List<PracticeAssessmentAnswer> practiceAssessmentAnswers;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
