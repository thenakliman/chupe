package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.TaskState;

import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpsertTaskDTO {
  @Size(min = 10, max = 256)
  private String description;
  private TaskState state;
  private int progress;
  private Date startedOn;
  private Date endedOn;
}
