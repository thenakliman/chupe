package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.FeedbackSessionService;

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
      @RequestBody UpsertFeedbackSessionDTO feedbackSession) {
    feedbackSessionService.createSession(feedbackSession, getUsername());
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/feedback-sessions/{feedbackSessionId}")
  public ResponseEntity updateFeedbackSessions(
      @PathVariable Long feedbackSessionId,
      @RequestBody UpsertFeedbackSessionDTO feedbackSession) throws NotFoundException {

    feedbackSessionService.updateSession(feedbackSessionId, feedbackSession, getUsername());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private String getUsername() {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUsername();
  }
}
