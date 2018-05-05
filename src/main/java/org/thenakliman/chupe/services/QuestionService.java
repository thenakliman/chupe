package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;

@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionsRepository;

  public Question addQuestion(Question question) {
    return questionsRepository.save(question);
  }
}
