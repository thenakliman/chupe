package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.RetroPointType;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpsertRetroPointDTO {
  private long retroId;

  @Size(min = 10, max = 256)
  private String description;

  private RetroPointType type;
}
