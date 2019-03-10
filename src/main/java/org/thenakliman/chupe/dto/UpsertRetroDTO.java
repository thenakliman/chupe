package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertRetroDTO {
  @NonNull
  private String name;
  @NonNull
  private Long maximumVote;
}
