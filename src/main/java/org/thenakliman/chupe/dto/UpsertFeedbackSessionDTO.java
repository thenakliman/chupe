package org.thenakliman.chupe.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertFeedbackSessionDTO {
  @Size(min = 10, max = 256)
  private String description;
}
