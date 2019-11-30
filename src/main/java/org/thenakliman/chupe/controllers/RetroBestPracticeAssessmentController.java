package org.thenakliman.chupe.controllers;

import static org.thenakliman.chupe.common.utils.Utils.emptyListIfNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.services.BestPracticeAssessmentService;

@Controller
public class RetroBestPracticeAssessmentController extends BaseController {
  private BestPracticeAssessmentService bestPracticeAssessmentService;

  public RetroBestPracticeAssessmentController(BestPracticeAssessmentService bestPracticeAssessmentService) {
    this.bestPracticeAssessmentService = bestPracticeAssessmentService;
  }

  @PostMapping("/retros/{retroId}/best-practices-assessments")
  public ResponseEntity<BestPracticeAssessmentDTO> createPractice(
      @PathVariable Long retroId,
      @RequestBody List<BestPracticeAssessmentAnswerDTO> bestPracticeAssessmentAnswerDTOs) {

    BestPracticeAssessmentDTO bestPracticeAssessmentDTOs = bestPracticeAssessmentService.saveBestPracticeAssessment(
        retroId,
        emptyListIfNull(bestPracticeAssessmentAnswerDTOs),
        getRequestUsername());

    return new ResponseEntity<>(bestPracticeAssessmentDTOs, HttpStatus.CREATED);
  }
}
