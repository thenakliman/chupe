package org.thenakliman.chupe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.thenakliman.chupe.models.ActionItemStatus;

import java.util.Collections;
import java.util.List;

@Data
public class ActionItemQueryParams {
  @JsonProperty("retro")
  private Long retro;
  @JsonProperty("assignedTo")
  private String assignedTo;
  @JsonProperty("statuses")
  private List<ActionItemStatus> statuses = Collections.emptyList();
}
