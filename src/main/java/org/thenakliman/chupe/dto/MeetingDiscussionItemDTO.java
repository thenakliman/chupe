package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.Data;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.DiscussionItemType;

@Data
public class MeetingDiscussionItemDTO {
  private Long id;
  private String discussionItem;
  private DiscussionItemType discussionItemType;
  private ActionItemStatus status;
  private String createdBy;
  private String assignedTo;
  private Long meetingId;
  private Date deadlineToAct;
}
