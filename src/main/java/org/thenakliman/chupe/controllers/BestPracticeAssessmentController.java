package org.thenakliman.chupe.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.services.BestPracticeAssessmentService;

@Controller
public class BestPracticeAssessmentController extends BaseController {
  private BestPracticeAssessmentService bestPracticeAssessmentService;

  public BestPracticeAssessmentController(BestPracticeAssessmentService bestPracticeAssessmentService) {
    this.bestPracticeAssessmentService = bestPracticeAssessmentService;
  }

  @PostMapping("/best-practices-assessments")
  public ResponseEntity<List<BestPracticeAssessmentDTO>> createPractice(
      @RequestBody List<UpsertBestPracticeAssessmentDTO> upsertBestPracticeAssessmentDTOs) {

    List<BestPracticeAssessmentDTO> bestPracticeAssessmentDTOs = bestPracticeAssessmentService.saveBestPracticeAssessment(
        upsertBestPracticeAssessmentDTOs,
        getRequestUsername());

    return new ResponseEntity<>(bestPracticeAssessmentDTOs, HttpStatus.OK);
  }
}
