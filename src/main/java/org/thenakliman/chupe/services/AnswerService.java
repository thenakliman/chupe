package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.AnswerRepository;
import org.thenakliman.chupe.transformer.AnswerTransformer;

@Service
public class AnswerService {
  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private DateUtil dateUtil;

  @Autowired
  private AnswerTransformer answerTransformer;

  /** Get answer of the given question id.
   *
   * @param questionId id of the question for which answer has to be returned
   * @return list of answers are returned
   * @throws NotFoundException Raises if no answers are found for the question
   */
  public List<AnswerDTO> getAnswersOfGivenQuestion(long questionId) throws NotFoundException {
    List<Answer> answers = answerRepository.findByQuestionId(questionId);
    if (answers == null) {
      throw new NotFoundException("Answer does not exist for question id " + questionId);
    }

    return answerTransformer.transformToAnswerDTOs(answers);
  }

  /** Create answer for a question.
   *
   * @param answerDTO of the question
   * @return answer after saving with enriched data like id, create at time
   */
  public AnswerDTO addAnswer(AnswerDTO answerDTO) {
    return answerTransformer.transformToAnswerDTO(
        answerRepository.save(answerTransformer.transformToAnswer(answerDTO)));
  }

  /**
   * Updates answer.
   * @param id of the answer to update
   * @param answer new updated fields
   * @return updated answer
   * @throws NotFoundException raised when answer does not exist
   */
  public AnswerDTO updateAnswer(Long id, AnswerDTO answer) throws NotFoundException {

    Optional<Answer> existingAnswer = answerRepository.findById(id);
    if (!existingAnswer.isPresent()) {
      throw new NotFoundException("Answer with id " + id + " could not be found");
    }

    existingAnswer.get().setUpdatedAt(dateUtil.getTime());
    existingAnswer.get().setId(id);
    existingAnswer.get().setAnswer(answer.getAnswer());
    existingAnswer.get().setQuestionId(answer.getQuestionId());

    User answeredBy = new User();
    answeredBy.setUserName(answer.getAnsweredBy());
    existingAnswer.get().setAnsweredBy(answeredBy);
    return answerTransformer.transformToAnswerDTO(answerRepository.save(existingAnswer.get()));
  }
}
