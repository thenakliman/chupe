package org.thenakliman.chupe.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.ActionItemStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InsertActionItemDTO {
  private long retroId;
  private Long retroPointId;
  @Size(min = 10, max = 2000)
  private String description;
  @Size(min = 1, max = 256)
  private String assignedTo;
  @NotNull
  private Date deadlineToAct;
}
