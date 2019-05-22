package org.thenakliman.chupe.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.thenakliman.chupe.models.ActionItemStatus;

@Data
public class UpdateActionItemDTO {
  @Size(min = 10, max = 2000)
  private String description;
  private ActionItemStatus status;
  @Size(min = 1, max = 256)
  private String assignedTo;
  @NotNull
  private Date deadlineToAct;
}
