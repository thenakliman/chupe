package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.AnswerRepository;

@Service
public class AnswerService {
  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private DateUtil dateUtil;

  @Autowired
  private ConverterUtil converterUtil;

  public AnswerService() {
  }

  public List<AnswerDTO> getAnswers(long questionId) throws NotFoundException {
    List<Answer> answers = answerRepository.findByQuestionId(questionId);
    if (answers.isEmpty()) {
      throw new NotFoundException(format("Answer does not exist for question id %d", questionId));
    }

    return converterUtil.convertToListOfObjects(answers, AnswerDTO.class);
  }

  public AnswerDTO addAnswer(AnswerDTO answerDTO) {
    Answer answer = converterUtil.convertToObject(answerDTO, Answer.class);
    Answer savedAnswer = answerRepository.save(answer);
    return converterUtil.convertToObject(savedAnswer, AnswerDTO.class);
  }

  public AnswerDTO updateAnswer(Long id, AnswerDTO answer) throws NotFoundException {

    Optional<Answer> existingAnswer = answerRepository.findById(id);
    if (existingAnswer.isPresent()) {
      existingAnswer.get().setUpdatedAt(dateUtil.getTime());
      existingAnswer.get().setId(id);
      existingAnswer.get().setAnswer(answer.getAnswer());
      existingAnswer.get().setQuestionId(answer.getQuestionId());

      User answeredBy = new User();
      answeredBy.setUserName(answer.getAnsweredBy());
      existingAnswer.get().setAnsweredBy(answeredBy);
      return converterUtil.convertToObject(answerRepository.save(existingAnswer.get()), AnswerDTO.class);
    }

    throw new NotFoundException(format("Answer with id %d could not be found", id));
  }
}
