package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.models.Answer;
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
  public ResponseEntity<List<Answer>> getAnswerOfGivenQuestion(
          @RequestParam("questionId") long id) throws NotFoundException {
    try {
      return new ResponseEntity<>(answerService.getAnswersOfGivenQuestion(id), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/answers")
  public ResponseEntity<Answer> addAnswer(@RequestHeader HttpHeaders header,
                                          @RequestBody Answer answer) {
    Answer createdAnswer = answerService.addAnswer(answer);
    return new ResponseEntity<>(createdAnswer, HttpStatus.OK);
  }
}