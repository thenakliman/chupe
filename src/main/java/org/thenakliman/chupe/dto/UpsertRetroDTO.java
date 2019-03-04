package org.thenakliman.chupe.dto;

import lombok.*;

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
