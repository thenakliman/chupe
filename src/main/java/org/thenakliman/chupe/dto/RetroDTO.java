package org.thenakliman.chupe.dto;

import lombok.*;
import org.thenakliman.chupe.models.RetroStatus;

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
  private RetroStatus status;
}
