package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestPracticeDTO {
  private Long id;
  private String description;
  private String needImprovement;
  private String doneWell;
  private boolean applicable;
}

