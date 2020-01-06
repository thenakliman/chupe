package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.ActionItem;

import java.util.List;

@Service
public class ActionItemService {
  private RetroActionItemService retroActionItemService;
  private MeetingService meetingService;

  @Autowired
  public ActionItemService(RetroActionItemService retroActionItemService, MeetingService meetingService) {
    this.retroActionItemService = retroActionItemService;
    this.meetingService = meetingService;
  }

  public List<ActionItem> getActionItems(String userName) {
    List<ActionItem> activeActionItem = retroActionItemService.getActiveActionItem(userName);
    activeActionItem.addAll(meetingService.getMeetingActionItem(userName));
    return activeActionItem;
  }
}
