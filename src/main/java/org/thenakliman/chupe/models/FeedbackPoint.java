package org.thenakliman.chupe.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "feedback_point")
@Table
public class FeedbackPoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "given_by", referencedColumnName = "username")
  private User givenBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "given_to", referencedColumnName = "username")
  private User givenTo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feedback_session", referencedColumnName = "id")
  private FeedbackSession feedbackSession;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
