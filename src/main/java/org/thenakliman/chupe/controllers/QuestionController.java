package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  @PostMapping("/question")
  public ResponseEntity<Question> askQuestion(@RequestHeader HttpHeaders header,
                                              @RequestBody Question question) {

    Question createdQuestion = questionService.addQuestion(question);
    return new ResponseEntity<>(createdQuestion, HttpStatus.OK);
  }
}
