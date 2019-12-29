package org.thenakliman.chupe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.services.PracticesAssessmentService;

import java.util.List;

import static org.thenakliman.chupe.common.utils.Utils.emptyListIfNull;

@Controller
public class PracticesAssessmentController extends BaseController {
  private PracticesAssessmentService practicesAssessmentService;

  public PracticesAssessmentController(PracticesAssessmentService practicesAssessmentService) {
    this.practicesAssessmentService = practicesAssessmentService;
  }

  @PostMapping("/retros/{retroId}/practices-assessment")
  public ResponseEntity<BestPracticeAssessmentDTO> createPractice(
          @PathVariable Long retroId,
          @RequestBody List<BestPracticeAssessmentAnswerDTO> bestPracticeAssessmentAnswerDTOs) {

    BestPracticeAssessmentDTO bestPracticeAssessmentDTOs = practicesAssessmentService.saveBestPracticeAssessment(
            retroId,
            emptyListIfNull(bestPracticeAssessmentAnswerDTOs),
            getRequestUsername());

    return new ResponseEntity<>(bestPracticeAssessmentDTOs, HttpStatus.CREATED);
  }

  @GetMapping("/retros/{retroId}/practices-assessment")
  public ResponseEntity<List<BestPracticeAssessmentAnswerDTO>> createPractice(@PathVariable Long retroId) {

    List<BestPracticeAssessmentAnswerDTO> practicesAssessment = practicesAssessmentService.getPracticesAssessment(
            retroId,
            getRequestUsername());

    return new ResponseEntity<>(practicesAssessment, HttpStatus.OK);
  }
}
