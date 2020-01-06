package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.services.ActionItemService;

import java.util.List;

@Controller
public class ActionItemController extends BaseController {
  private ActionItemService actionItemService;

  @Autowired
  public ActionItemController(ActionItemService actionItemService) {
    this.actionItemService = actionItemService;
  }

  @GetMapping("/action-items")
  public ResponseEntity<List<ActionItem>> getActionItems() {
    return new ResponseEntity<>(actionItemService.getActionItems(getRequestUsername()), HttpStatus.OK);
  }
}