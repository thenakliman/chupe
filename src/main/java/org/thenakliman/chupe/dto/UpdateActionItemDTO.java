package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import org.thenakliman.chupe.models.ActionItemStatus;

@Data
public class UpdateActionItemDTO {
  private String description;
  private ActionItemStatus status;
  private String assignedTo;
  private Date deadlineToAct;
}
