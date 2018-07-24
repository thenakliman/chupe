package org.thenakliman.chupe.transformer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;


@Component
public class QuestionTransformer {
  /** transform list of question to list of question dtos.
   *
   * @param questions list of question model
   * @return list of question dto
   */
  public List<QuestionDTO> transformToQuestionDTO(Iterable<Question> questions) {
    List<QuestionDTO> questionDTOs = new ArrayList<QuestionDTO>();
    if (questions == null) {
      return questionDTOs;
    }

    questions.forEach(question -> {
      questionDTOs.add(new QuestionDTO(
          question.getId(),
          question.getQuestion(),
          question.getDescription(),
          question.getOwner(),
          question.getAssignedTo(),
          question.getStatus(),
          question.getPriority()));
    });

    return questionDTOs;
  }

  /** Transform Question model to QuestionDTO.
   *
   * @param question question model
   * @return questionDTO
   */
  public QuestionDTO transformToQuestionDTO(Question question) {
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setId(question.getId());
    questionDTO.setQuestion(question.getQuestion());
    questionDTO.setDescription(question.getDescription());
    questionDTO.setOwner(question.getOwner());
    questionDTO.setAssignedTo(question.getAssignedTo());
    questionDTO.setStatus(question.getStatus());
    questionDTO.setPriority(question.getPriority());

    return questionDTO;
  }
}
