package org.thenakliman.chupe.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.dto.UpsertAnswerDTO;
import org.thenakliman.chupe.services.AnswerService;

@Controller
public class AnswerController extends BaseController {
  private AnswerService answerService;

  @Autowired
  public AnswerController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @GetMapping("/answers")
  public ResponseEntity<List<AnswerDTO>> getAnswerOfGivenQuestion(@RequestParam("questionId") long id) {
    return new ResponseEntity<>(answerService.getAnswers(id), HttpStatus.OK);
  }

  @PostMapping("/answers")
  public ResponseEntity<AnswerDTO> addAnswer(@Valid @RequestBody UpsertAnswerDTO upsertAnswerDTO) {
    AnswerDTO createdAnswer = answerService.addAnswer(upsertAnswerDTO, getRequestUsername());
    return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
  }

  @PutMapping("/answers/{id}")
  public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable(value = "id") Long id,
                                                @Valid @RequestBody UpsertAnswerDTO upsertAnswerDTO) {
    AnswerDTO updatedAnswer = answerService.updateAnswer(id, upsertAnswerDTO, getRequestUsername());
    return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
  }
}