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

  @Autowired
  AnswerService answerService;

  /** API for fetching all the answer.
   *
   * @param id of the question for which answer has to be fetched
   * @return List of answers
   * @throws NotFoundException Raises when no question is found
   */
  @GetMapping("/answers")
  public ResponseEntity<List<AnswerDTO>> getAnswerOfGivenQuestion(
          @RequestParam("questionId") long id) throws NotFoundException {
    try {
      return new ResponseEntity<>(answerService.getAnswersOfGivenQuestion(id), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * API to create an answer.
   * @param answerDTO answer to create
   * @return created answer
   */
  @PostMapping("/answers")
  public ResponseEntity<AnswerDTO> addAnswer(@RequestBody AnswerDTO answerDTO) {
    AnswerDTO createdAnswer = answerService.addAnswer(answerDTO);
    return new ResponseEntity<>(createdAnswer, HttpStatus.OK);
  }

  /**
   * Update an exising question.
   * @param id of the answer to update
   * @param answerDTO updated answer
   * @return updated answer
   */
  @PutMapping("/answers/{id}")
  public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable(value = "id") Long id,
                                              @RequestBody AnswerDTO answerDTO) {
    try {
      return new ResponseEntity<>(answerService.updateAnswer(id, answerDTO), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}