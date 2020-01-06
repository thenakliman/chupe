package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.FeedbackSessionService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class FeedbackSessionController extends BaseController {
  private FeedbackSessionService feedbackSessionService;

  @Autowired
  public FeedbackSessionController(FeedbackSessionService feedbackSessionService) {
    this.feedbackSessionService = feedbackSessionService;
  }

  @GetMapping("/feedback-sessions")
  public ResponseEntity<List<FeedbackSessionDTO>> getFeedbackSessions() {
    List<FeedbackSessionDTO> feedbackSessions = feedbackSessionService.getFeedbackSessions(
        getUsername());
    return new ResponseEntity<>(feedbackSessions, HttpStatus.OK);
  }

  @PostMapping("/feedback-sessions")
  public ResponseEntity createFeedbackSessions(
      @RequestBody @Valid UpsertFeedbackSessionDTO feedbackSession) {
    feedbackSessionService.createSession(feedbackSession, getUsername());
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/feedback-sessions/{feedbackSessionId}")
  public ResponseEntity updateFeedbackSessions(
      @PathVariable @NotNull Long feedbackSessionId,
      @RequestBody @Valid UpsertFeedbackSessionDTO feedbackSession) {

    feedbackSessionService.updateSession(feedbackSessionId, feedbackSession, getUsername());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private String getUsername() {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUsername();
  }
}
