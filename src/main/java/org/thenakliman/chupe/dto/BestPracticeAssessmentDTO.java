package org.thenakliman.chupe.dto;

import java.util.List;

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
  List<BestPracticeAssessmentAnswerDTO> answers;
}

