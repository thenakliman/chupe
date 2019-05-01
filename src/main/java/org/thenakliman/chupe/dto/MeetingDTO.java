package org.thenakliman.chupe.dto;

import lombok.Data;

@Data
public class MeetingDTO {
  private Long id;
  private String subject;
  private String createdBy;
}
