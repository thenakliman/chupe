package org.thenakliman.chupe.dto;

import lombok.Data;
import org.thenakliman.chupe.models.DiscussionItemType;

@Data
public class MeetingDiscussionItemDTO {
  private Long id;
  private String discussionItem;
  private DiscussionItemType discussionItemType;
  private String createdBy;
  private String assignedTo;
  private Long meetingId;
}
