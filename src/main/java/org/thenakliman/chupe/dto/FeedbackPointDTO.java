package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackPointDTO {
  private long sessionId;
  private long id;
  private String description;
  private String givenTo;
  private String givenBy;
}
