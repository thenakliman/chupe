package org.thenakliman.chupe.services;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.repositories.AnswerRepository;

@Service
public class AnswerService {
  @Autowired
  private AnswerRepository answerRepository;

  /** Get answer of the given question id.
   *
   * @param questionId id of the question for which answer has to be returned
   * @return list of answers are returned
   * @throws NotFoundException Raises if no answers are found for the question
   */
  public List<Answer> getAnswersOfGivenQuestion(long questionId) throws NotFoundException {
    List<Answer> answers = answerRepository.findByQuestionId(questionId);
    if (answers == null) {
      throw new NotFoundException("Answer does not exist for question id " + questionId);
    }

    return answers;
  }

  /** Create answer for a question.
   *
   * @param answer of the question
   * @return answer after saving with enriched data like id, create at time
   */
  public Answer addAnswer(Answer answer) {
    return answerRepository.save(answer);
  }
}
