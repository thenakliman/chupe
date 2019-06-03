package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thenakliman.chupe.models.RetroStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateRetroStatusDto {
  private RetroStatus status;
}
