package org.thenakliman.chupe.dto;

import java.util.Date;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.TaskState;

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
