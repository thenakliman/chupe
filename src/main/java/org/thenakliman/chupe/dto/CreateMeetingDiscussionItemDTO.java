package org.thenakliman.chupe.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.thenakliman.chupe.models.DiscussionItemType;

@Data
public class CreateMeetingDiscussionItemDTO {
  @NotNull
  private Long meetingId;
  @NotNull
  private String discussionItem;
  @NotNull
  private DiscussionItemType discussionItemType;
  @NotNull
  private String assignedTo;
}
