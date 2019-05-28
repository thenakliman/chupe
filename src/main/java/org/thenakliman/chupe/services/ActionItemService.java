package org.thenakliman.chupe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.ActionItem;

@Service
public class ActionItemService {
  private RetroActionItemService retroActionItemService;

  @Autowired
  public ActionItemService(RetroActionItemService retroActionItemService) {
    this.retroActionItemService = retroActionItemService;
  }

  public List<ActionItem> getActionItems(String userName) {
    return retroActionItemService.getActiveActionItem(userName);
  }
}
