package org.thenakliman.chupe.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.thenakliman.chupe.models.DiscussionItemType;

@Data
public class UpdateMeetingDiscussionItemDTO {
  @NotNull
  private String discussionItem;
  @NotNull
  private DiscussionItemType discussionItemType;
  @NotNull
  private String assignedTo;
}
