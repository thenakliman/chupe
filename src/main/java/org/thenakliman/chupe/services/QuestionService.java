package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
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

  /** Update a question.
   *
   * @param id question id
   * @param question question dto containing new question details
   * @throws NotFoundException if question does not exist, we are trying to update
   */
  public void updateQuestions(long id, QuestionDTO question) throws NotFoundException {

    Optional<Question> existingQuestion = questionsRepository.findById(id);
    if (existingQuestion == null) {
      throw new NotFoundException("Question with id " + id + " could not be found");
    }

    Question updatedQuestion = new Question();
    updatedQuestion.setId((int)id);
    updatedQuestion.setAssignedTo(question.getAssignedTo());
    updatedQuestion.setDescription(question.getDescription());
    updatedQuestion.setOwner(question.getOwner());
    updatedQuestion.setQuestion(question.getQuestion());
    updatedQuestion.setStatus(question.getStatus());
    updatedQuestion.setPriority(question.getPriority());
    if (existingQuestion.isPresent()) {
      updatedQuestion.setCreatedAt(existingQuestion.get().getCreatedAt());
    } else {
      updatedQuestion.setCreatedAt(new DateUtil().getTime());
    }
    questionsRepository.save(updatedQuestion);
  }
}