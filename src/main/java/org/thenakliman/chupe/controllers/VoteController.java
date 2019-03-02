package org.thenakliman.chupe.controllers;

import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.RetroPointService;


@Controller
public class VoteController extends BaseController {
  private RetroPointService retroPointService;

  public VoteController(RetroPointService retroPointService) {
    this.retroPointService = retroPointService;
  }

  @PostMapping("/retro-point-votes/{id}")
  public ResponseEntity castVote(@PathVariable(value = "id") long id) {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    try {
      retroPointService.castVote(id, userDetails.getUsername());
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (BadHttpRequest ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
