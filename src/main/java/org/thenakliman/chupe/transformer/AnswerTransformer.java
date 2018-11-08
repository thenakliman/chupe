package org.thenakliman.chupe.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;

@Component
public class AnswerTransformer {
  @Autowired
  private DateUtil dateUtil;

  /**
   * Transform to answerDTO from answer model.
   * @param answer to be transformed
   * @return transformed answer
   */
  public AnswerDTO transformToAnswerDTO(Answer answer) {
    return AnswerDTO.builder()
        .answer(answer.getAnswer())
        .answeredBy(answer.getAnsweredBy().getUserName())
        .questionId(answer.getQuestionId())
        .id(answer.getId())
        .build();
  }

  /**
   * Transform to Answer from AnswerDTO.
   * @param answerDTO answer dto to be transformed
   * @return transformed object
   */
  public Answer transformToAnswer(AnswerDTO answerDTO) {
    Answer answer = new Answer();
    answer.setId(answerDTO.getId());
    answer.setAnswer(answerDTO.getAnswer());
    answer.setQuestionId(answerDTO.getQuestionId());
    User user = new User();
    user.setUserName(answerDTO.getAnsweredBy());
    answer.setAnsweredBy(user);
    answer.setCreatedAt(dateUtil.getTime());
    answer.setUpdatedAt(dateUtil.getTime());
    return answer;
  }

  /**
   * Transforms list of AnswerDTOs to answer model.
   * @param answers to transform
   * @return transformed answers
   */
  public List<AnswerDTO> transformToAnswerDTOs(List<Answer> answers) {
    return answers.stream().map(this::transformToAnswerDTO).collect(Collectors.toList());
  }
}
