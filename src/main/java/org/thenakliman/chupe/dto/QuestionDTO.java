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
public class QuestionDTO {
  private long id;

  private String question;

  private String description;

  private String owner;

  private String assignedTo;

  private QuestionStatus status;

  private QuestionPriority priority;
}
