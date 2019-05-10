package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
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
import org.thenakliman.chupe.services.AnswerService;

@Controller
public class AnswerController extends BaseController {
  private AnswerService answerService;

  @Autowired
  public AnswerController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @GetMapping("/answers")
  public ResponseEntity<List<AnswerDTO>> getAnswerOfGivenQuestion(
      @RequestParam("questionId") long id) throws NotFoundException {
    try {
      return new ResponseEntity<>(answerService.getAnswers(id), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/answers")
  public ResponseEntity<AnswerDTO> addAnswer(@RequestBody AnswerDTO answerDTO) {
    AnswerDTO createdAnswer = answerService.addAnswer(answerDTO);
    return new ResponseEntity<>(createdAnswer, HttpStatus.OK);
  }

  @PutMapping("/answers/{id}")
  public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable(value = "id") Long id,
                                                @RequestBody AnswerDTO answerDTO) {
    try {
      AnswerDTO updatedAnswer = answerService.updateAnswer(id, answerDTO, getRequestUsername());
      return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}