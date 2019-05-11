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
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.services.QuestionService;


@Controller
public class QuestionController extends BaseController {
  @Autowired
  private QuestionService questionService;

  @PostMapping("/questions")
  public ResponseEntity<QuestionDTO> askQuestion(@RequestHeader HttpHeaders header,
                                                 @RequestBody UpsertQuestionDTO question) {

    QuestionDTO createdQuestion = questionService.addQuestion(question, getRequestUsername());
    return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
  }

  @PutMapping("/questions/{id}")
  public ResponseEntity updateQuestion(@RequestHeader HttpHeaders header,
                                       @PathVariable(value = "id") long id,
                                       @RequestBody UpsertQuestionDTO questionDTO) {
    HttpStatus httpStatus = HttpStatus.NO_CONTENT;
    try {
      QuestionDTO updatedQuestion = questionService.updateQuestions(id, questionDTO, getRequestUsername());
      return new ResponseEntity<>(updatedQuestion, httpStatus);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/questions")
  public ResponseEntity getQuestions() {
    try {
      return new ResponseEntity<>(questionService.getQuestions(), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
