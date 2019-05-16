package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpsertAnswerDTO {
  private String answer;
  private long questionId;
}
