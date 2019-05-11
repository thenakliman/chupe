package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpsertQuestionDTO {
  private String question;
  private String description;
  private String assignedTo;
  private QuestionStatus status;
  private QuestionPriority priority;
}
