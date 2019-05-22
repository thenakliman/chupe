package org.thenakliman.chupe.controllers;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.dto.ActionItemQueryParams;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.dto.UpdateActionItemDTO;
import org.thenakliman.chupe.services.ActionItemService;

@Controller
public class ActionItemController extends BaseController {
  private ActionItemService actionItemService;

  @Autowired
  public ActionItemController(ActionItemService actionItemService) {
    this.actionItemService = actionItemService;
  }

  @GetMapping("/retro-action-items")
  public ResponseEntity<List<ActionItemDTO>> getMeetings(ActionItemQueryParams actionItemQueryParams) {
    List<ActionItemDTO> actionItemDTOs = actionItemService.getActionItems(actionItemQueryParams);
    return new ResponseEntity<>(actionItemDTOs, HttpStatus.OK);
  }

  @PostMapping("/retro-action-items")
  public ResponseEntity<ActionItemDTO> addActionItem(@Valid @RequestBody InsertActionItemDTO insertActionItemDTO) {
    ActionItemDTO savedActionItem = actionItemService.addActionItem(insertActionItemDTO, getRequestUsername());
    return new ResponseEntity<>(savedActionItem, HttpStatus.CREATED);
  }

  @PutMapping("/retro-action-items/{actionItemId}")
  public ResponseEntity<ActionItemDTO> updateMeeting(@NotNull @PathVariable Long actionItemId,
                                                     @Valid @RequestBody UpdateActionItemDTO updateActionItemDTO) {

    ActionItemDTO updatedMeeting = actionItemService.updateActionItem(
        actionItemId, updateActionItemDTO, getRequestUsername());

    return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
  }
}
