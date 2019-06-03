package org.thenakliman.chupe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.RetroPointService;


@Controller
public class VoteController extends BaseController {
  private final RetroPointService retroPointService;

  public VoteController(RetroPointService retroPointService) {
    this.retroPointService = retroPointService;
  }

  @PostMapping("/retro-point-votes/{retroPointId}")
  @PreAuthorize("@retroValidationService.canBeVoted(#retroPointId)")
  public ResponseEntity castVote(@PathVariable(value = "retroPointId") long retroPointId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    retroPointService.castVote(retroPointId, user.getUsername());
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
