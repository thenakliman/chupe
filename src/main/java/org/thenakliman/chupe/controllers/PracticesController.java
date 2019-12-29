package org.thenakliman.chupe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.services.PracticeService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PracticesController extends BaseController {
  private PracticeService practiceService;

  public PracticesController(PracticeService practiceService) {
    this.practiceService = practiceService;
  }

  @GetMapping("/best-practices")
  public ResponseEntity<List<BestPracticeDTO>> getPractices() {
    return new ResponseEntity<>(practiceService.getActiveBestPractices(), HttpStatus.OK);
  }

  @PostMapping("/best-practices")
  public ResponseEntity<BestPracticeDTO> createPractice(@Valid @RequestBody UpsertBestPracticeDTO upsertBestPracticeDTO) {
    BestPracticeDTO bestPractices = practiceService.saveBestPractice(upsertBestPracticeDTO, getRequestUsername());
    return new ResponseEntity<>(bestPractices, HttpStatus.CREATED);
  }
}
