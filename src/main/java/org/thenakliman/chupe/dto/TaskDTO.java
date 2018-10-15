package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.TaskState;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskDTO {
  private long id;

  private String description;

  private TaskState state;

  private int progress;

  private String createdBy;

  private Date startedOn;

  private Date endedOn;
}
