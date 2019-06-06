package org.thenakliman.chupe.controllers;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.thenakliman.chupe.services.RetroActionItemService;

@Controller
public class RetroActionItemController extends BaseController {
  private RetroActionItemService retroActionItemService;

  @Autowired
  public RetroActionItemController(RetroActionItemService retroActionItemService) {
    this.retroActionItemService = retroActionItemService;
  }

  @GetMapping("/retro-action-items")
  public ResponseEntity<List<ActionItemDTO>> getMeetings(ActionItemQueryParams actionItemQueryParams) {
    List<ActionItemDTO> actionItemDTOs = retroActionItemService.getActionItems(actionItemQueryParams);
    return new ResponseEntity<>(actionItemDTOs, HttpStatus.OK);
  }

  @PostMapping("/retro-action-items")
  @PreAuthorize("@retroValidationService.isRetroInProgress(#insertActionItemDTO.getRetroId())")
  public ResponseEntity<ActionItemDTO> addActionItem(@Valid @RequestBody InsertActionItemDTO insertActionItemDTO) {
    ActionItemDTO savedActionItem = retroActionItemService.addActionItem(insertActionItemDTO, getRequestUsername());
    return new ResponseEntity<>(savedActionItem, HttpStatus.CREATED);
  }

  @PutMapping("/retro-action-items/{actionItemId}")
  @PreAuthorize("@retroValidationService.canActionItemBeUpdated(#actionItemId)")
  public ResponseEntity<ActionItemDTO> updateMeeting(@NotNull @PathVariable Long actionItemId,
                                                     @Valid @RequestBody UpdateActionItemDTO updateActionItemDTO) {

    ActionItemDTO updatedMeeting = retroActionItemService.updateActionItem(
        actionItemId, updateActionItemDTO, getRequestUsername());

    return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
  }
}
