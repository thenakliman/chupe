package org.thenakliman.chupe.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.services.QuestionService;


@Controller
public class QuestionController extends BaseController {
  @Autowired
  private QuestionService questionService;

  /** API for creating a question.
   *
   * <p> Accepts following questions details </p>
   * 1. Question, summary.
   * 2. Description, of the question or additional data needed to explain question
   * 3. Owner, user asked the question
   * 4. AssignedTO, user responsible for answering the question */
  @PostMapping("/questions")
  public ResponseEntity<QuestionDTO> askQuestion(@RequestHeader HttpHeaders header,
                                              @RequestBody Question question) {

    QuestionDTO createdQuestion = questionService.addQuestion(question);
    return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
  }

  /** API for updating a question.
   *
   * <p> Accepts following questions details </p>
   * 1. Question, summary.
   * 2. Description, of the question or additional data needed to explain question
   * 3. Owner, user asked the question
   * 4. AssignedTO, user responsible for answering the question */
  @PutMapping("/questions/{id}")
  public ResponseEntity updateQuestion(@RequestHeader HttpHeaders header,
                                       @PathVariable(value = "id") long id,
                                       @RequestBody QuestionDTO questionDTO) {
    HttpStatus httpStatus = HttpStatus.NO_CONTENT;
    try {
      questionService.updateQuestions(id, questionDTO);
    } catch (NotFoundException ex) {
      httpStatus = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(httpStatus);
  }

  /** APIs for fetching list of questions.
   *
   * @return list of questions
   */
  @GetMapping("/questions")
  public ResponseEntity getQuestions() {
    try {
      return new ResponseEntity<>(questionService.getQuestions(), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
