package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

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
