package org.thenakliman.chupe.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;
import org.thenakliman.chupe.transformer.QuestionTransformer;


@Service
public class QuestionService {

  @Autowired
  private QuestionTransformer questionTransformer;

  @Autowired
  private QuestionRepository questionsRepository;

  public QuestionDTO addQuestion(Question question) {
    return questionTransformer.transformToQuestionDTO(questionsRepository.save(question));
  }

  public List<QuestionDTO> getQuestions() {
    return questionTransformer.transformToQuestionDTO(questionsRepository.findAll());
  }
}
