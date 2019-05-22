package org.thenakliman.chupe.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
