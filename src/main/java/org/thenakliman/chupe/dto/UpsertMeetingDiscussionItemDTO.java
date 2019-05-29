package org.thenakliman.chupe.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.thenakliman.chupe.models.DiscussionItemType;

@Data
public class UpsertMeetingDiscussionItemDTO {
  private Long meetingId;
  @Size(min = 10, max = 256)
  private String discussionItem;
  @NotNull
  private DiscussionItemType discussionItemType;
  @Size(min = 1, max = 256)
  private String assignedTo;
  private Date deadlineToAct;
}
