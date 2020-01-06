package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertFeedbackPointDTO {
  private long sessionId;
  @NotNull
  @Size(min = 10, max = 256)
  private String description;
  @NotNull
  @Size(min = 1, max = 256)
  private String givenTo;
}
