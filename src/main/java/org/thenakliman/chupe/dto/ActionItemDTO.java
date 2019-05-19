package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.ActionItemStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActionItemDTO {
  private Long id;
  private String description;
  private ActionItemStatus status;
  private String assignedTo;
  private Date deadlineToAct;
  private String createdBy;
}
