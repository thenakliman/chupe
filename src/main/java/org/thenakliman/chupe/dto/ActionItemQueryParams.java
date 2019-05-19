package org.thenakliman.chupe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.thenakliman.chupe.models.ActionItemStatus;

@Data
public class ActionItemQueryParams {
  @JsonProperty("retro")
  private Long retro;
  @JsonProperty("assignedTo")
  private String assignedTo;
  @JsonProperty("statuses")
  private List<ActionItemStatus> statuses = Collections.emptyList();
}
