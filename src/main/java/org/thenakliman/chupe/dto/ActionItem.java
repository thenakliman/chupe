package org.thenakliman.chupe.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ActionItem {
  private long id;
  private String description;
  private ActionItemType type;
  private Date deadlineToAct;
}
