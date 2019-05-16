package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.QuestionRepository;


@Service
public class QuestionService {
  private QuestionRepository questionsRepository;
  private Converter converter;
  private DateUtil dateUtil;

  @Autowired
  public QuestionService(QuestionRepository questionsRepository, Converter converter, DateUtil dateUtil) {
    this.questionsRepository = questionsRepository;
    this.converter = converter;
    this.dateUtil = dateUtil;
  }

  public QuestionDTO addQuestion(UpsertQuestionDTO questionDTO, String owner) {
    Question question = converter.convertToObject(questionDTO, Question.class);
    question.setOwner(User.builder().userName(owner).build());
    Question savedQuestion = questionsRepository.save(question);
    return converter.convertToObject(savedQuestion, QuestionDTO.class);
  }

  public List<QuestionDTO> getQuestions() {
    List<Question> questions = questionsRepository.findAll();
    return converter.convertToListOfObjects(questions, QuestionDTO.class);
  }

  public QuestionDTO updateQuestions(long id, UpsertQuestionDTO question, String owner) {
    Optional<Question> savedQuestionOptional = questionsRepository.findByIdAndOwnerUserName(id, owner);

    Question savedQuestion = savedQuestionOptional.orElseThrow(
        () -> new NotFoundException(String.format("Question %s not found or does not have permission to edit", id)));

    savedQuestion.setAssignedTo(User.builder().userName(question.getAssignedTo()).build());
    savedQuestion.setDescription(question.getDescription());
    savedQuestion.setOwner(User.builder().userName(owner).build());
    savedQuestion.setQuestion(question.getQuestion());
    savedQuestion.setStatus(question.getStatus());
    savedQuestion.setPriority(question.getPriority());
    savedQuestion.setUpdatedAt(dateUtil.getTime());
    Question updatedQuestion = questionsRepository.save(savedQuestion);
    return converter.convertToObject(updatedQuestion, QuestionDTO.class);
  }
}