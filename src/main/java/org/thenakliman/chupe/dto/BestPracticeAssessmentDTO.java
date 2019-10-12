package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestPracticeAssessmentDTO {
  private Long id;
  private Long bestPracticeId;
  private Long retroId;
  private Boolean answer;
}

