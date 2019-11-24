package org.thenakliman.chupe.controllers;

import javax.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.services.BestPracticeService;

@Controller
public class BestPracticesController extends BaseController {
  private BestPracticeService bestPracticeService;

  public BestPracticesController(BestPracticeService bestPracticeService) {
    this.bestPracticeService = bestPracticeService;
  }

  @GetMapping("/best-practices")
  public ResponseEntity<List<BestPracticeDTO>> getPractices() {
    return new ResponseEntity<>(bestPracticeService.getActiveBestPractices(), HttpStatus.OK);
  }

  @PostMapping("/best-practices")
  public ResponseEntity<BestPracticeDTO> createPractice(@Valid @RequestBody UpsertBestPracticeDTO upsertBestPracticeDTO) {
    BestPracticeDTO bestPractices = bestPracticeService.saveBestPractice(upsertBestPracticeDTO, getRequestUsername());
    return new ResponseEntity<>(bestPractices, HttpStatus.CREATED);
  }
}
