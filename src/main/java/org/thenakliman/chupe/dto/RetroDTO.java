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
public class RetroDTO {
  @NonNull
  private Long id;
  @NonNull
  private String name;
  @NonNull
  private String createdBy;
  @NonNull
  private Long maximumVote;
}
