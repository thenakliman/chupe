package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertRetroDTO {
  @Size(min = 10, max = 256)
  private String name;
  @Min(value = 1)
  private Long maximumVote;
}
