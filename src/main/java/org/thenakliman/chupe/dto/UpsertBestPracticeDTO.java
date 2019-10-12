package org.thenakliman.chupe.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpsertBestPracticeDTO {
  @NotNull
  private String description;
  @NotNull
  private String needImprovement;
  @NotNull
  private String doneWell;
  private boolean applicable;
}

