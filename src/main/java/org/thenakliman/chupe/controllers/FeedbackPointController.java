package org.thenakliman.chupe.controllers;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.services.FeedbackPointService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
public class FeedbackPointController extends BaseController {
  private FeedbackPointService feedbackPointService;

  @Autowired
  public FeedbackPointController(FeedbackPointService feedbackPointService) {
    this.feedbackPointService = feedbackPointService;
  }

  @GetMapping("/feedback-points")
  public ResponseEntity<List<FeedbackPointDTO>> getFeedbackPoints(
      @RequestParam @NonNull Long feedbackSessionId,
      @RequestParam(required = false) String givenTo) {

    List<FeedbackPointDTO> feedbackPoints;
    if (isNull(givenTo)) {
      feedbackPoints = feedbackPointService.getFeedbackPointsGivenToUser(
          getRequestUsername(),
          feedbackSessionId);
    } else {
      feedbackPoints = feedbackPointService.getFeedbackGivenToUserByAUser(
          getRequestUsername(),
          givenTo,
          feedbackSessionId);
    }
    return new ResponseEntity<>(feedbackPoints, HttpStatus.OK);
  }

  @PostMapping("/feedback-points")
  public ResponseEntity saveRetroPoint(@Valid @RequestBody UpsertFeedbackPointDTO feedbackPoint) {
    feedbackPointService.saveFeedbackPoint(getRequestUsername(), feedbackPoint);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/feedback-points/{retroPointId}")
  public ResponseEntity updateRetroPoint(
      @RequestBody @Valid UpsertFeedbackPointDTO feedbackPoint,
      @PathVariable @NotNull Long retroPointId) {

    feedbackPointService.updateFeedbackPoint(getRequestUsername(), retroPointId, feedbackPoint);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
