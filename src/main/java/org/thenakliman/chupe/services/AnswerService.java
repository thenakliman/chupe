package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.AnswerRepository;

@Service
public class AnswerService {
  private AnswerRepository answerRepository;

  private DateUtil dateUtil;

  private Converter converter;

  @Autowired
  public AnswerService(AnswerRepository answerRepository, DateUtil dateUtil, Converter converter) {
    this.answerRepository = answerRepository;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public List<AnswerDTO> getAnswers(long questionId) throws NotFoundException {
    List<Answer> answers = answerRepository.findByQuestionId(questionId);
    if (answers.isEmpty()) {
      throw new NotFoundException(format("Answer does not exist for question id %d", questionId));
    }

    return converter.convertToListOfObjects(answers, AnswerDTO.class);
  }

  public AnswerDTO addAnswer(AnswerDTO answerDTO) {
    Answer answer = converter.convertToObject(answerDTO, Answer.class);
    Answer savedAnswer = answerRepository.save(answer);
    return converter.convertToObject(savedAnswer, AnswerDTO.class);
  }

  public AnswerDTO updateAnswer(Long id, AnswerDTO answer, String createdBy) throws NotFoundException {

    Optional<Answer> savedAnswerOptional = answerRepository.findByIdAndAnsweredByUserName(id, createdBy);
    Answer savedAnswer = savedAnswerOptional.orElseThrow(
        () -> new NotFoundException(
            format("Either you don't have permission to edit or answer %s does not exist", id)));

    savedAnswer.setUpdatedAt(dateUtil.getTime());
    savedAnswer.setId(id);
    savedAnswer.setAnswer(answer.getAnswer());
    savedAnswer.setQuestion(Question.builder().id(answer.getQuestionId()).build());
    User answeredBy = new User();
    answeredBy.setUserName(answer.getAnsweredBy());
    savedAnswer.setAnsweredBy(answeredBy);
    return converter.convertToObject(answerRepository.save(savedAnswer), AnswerDTO.class);
  }
}
