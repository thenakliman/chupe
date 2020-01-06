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
