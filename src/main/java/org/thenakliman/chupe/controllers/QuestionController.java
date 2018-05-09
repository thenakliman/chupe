package org.thenakliman.chupe.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  @PostMapping("/question")
  public ResponseEntity<QuestionDTO> askQuestion(@RequestHeader HttpHeaders header,
                                              @RequestBody Question question) {

    QuestionDTO createdQuestion = questionService.addQuestion(question);
    return new ResponseEntity<>(createdQuestion, HttpStatus.OK);
  }

  /** APIs for fetching list of questions.
   *
   * @return list of questions
   */
  @GetMapping("/question")
  public ResponseEntity<List<QuestionDTO>> getQuestions() {
    try {
      return new ResponseEntity<>(questionService.getQuestions(), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
