package org.thenakliman.chupe.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpsertAnswerDTO {
  @Size(min = 10, max = 1000)
  private String answer;
  private long questionId;
}
