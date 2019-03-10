package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.RetroPointType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertRetroPointDTO {
  private long retroId;

  private String description;

  private RetroPointType type;
}
