package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.services.QuestionService;

import javax.validation.Valid;


@Controller
public class QuestionController extends BaseController {
  private QuestionService questionService;

  @Autowired
  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @PostMapping("/questions")
  public ResponseEntity<QuestionDTO> askQuestion(@Valid @RequestBody UpsertQuestionDTO question) {
    QuestionDTO createdQuestion = questionService.addQuestion(question, getRequestUsername());
    return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
  }

  @PutMapping("/questions/{id}")
  public ResponseEntity updateQuestion(@PathVariable(value = "id") long id,
                                       @Valid @RequestBody UpsertQuestionDTO questionDTO) {
    QuestionDTO updatedQuestion = questionService.updateQuestions(id, questionDTO, getRequestUsername());
    return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
  }

  @GetMapping("/questions")
  public ResponseEntity getQuestions() {
    return new ResponseEntity<>(questionService.getQuestions(), HttpStatus.OK);
  }
}
