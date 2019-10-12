package org.thenakliman.chupe.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.thenakliman.chupe.dto.ActionItemType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "meeting_discussion_item")
@Table
public class MeetingDiscussionItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "discussion_item", nullable = false)
  private String discussionItem;

  @Column(name = "type", nullable = false)
  private DiscussionItemType discussionItemType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ActionItemStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "createdBy", referencedColumnName = "username")
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignedTo", referencedColumnName = "username")
  private User assignedTo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id", referencedColumnName = "id")
  private Meeting meeting;

  @Column(name = "deadline_to_act", nullable = false)
  private Date deadlineToAct;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
