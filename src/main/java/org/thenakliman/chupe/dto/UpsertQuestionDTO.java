package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;

import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpsertQuestionDTO {
  @Size(min = 10, max = 100)
  private String question;
  @Size(min = 10, max = 1000)
  private String description;
  @Size(min = 1, max = 256)
  private String assignedTo;
  private QuestionStatus status;
  private QuestionPriority priority;
}
