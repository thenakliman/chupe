package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestPracticeAssessmentDTO {
  private Long id;
  List<BestPracticeAssessmentAnswerDTO> answers;
}

