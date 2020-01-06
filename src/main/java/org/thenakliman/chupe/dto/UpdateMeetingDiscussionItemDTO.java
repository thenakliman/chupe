package org.thenakliman.chupe.dto;

import lombok.Data;
import org.thenakliman.chupe.models.DiscussionItemType;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMeetingDiscussionItemDTO {
  @NotNull
  private String discussionItem;
  @NotNull
  private DiscussionItemType discussionItemType;
  @NotNull
  private String assignedTo;
}
