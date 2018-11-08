package org.thenakliman.chupe.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class AnswerTransformerTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private AnswerTransformer answerTransformer;

  @Test
  public void shouldTransformAnswerToAnswerDTO() {
    Answer answer = new Answer();
    Long questionId = 10L;
    answer.setQuestionId(questionId);
    Long id = 1L;
    answer.setId(id);
    String testAnswer = "test answer";
    answer.setAnswer(testAnswer);
    User user = new User();
    String username = "username";
    user.setUserName(username);
    answer.setAnsweredBy(user);

    AnswerDTO answerDTO = answerTransformer.transformToAnswerDTO(answer);
    assertThat(answerDTO, samePropertyValuesAs(
        getAnswerDTO(questionId, id, testAnswer, username)));
  }

  @Test
  public void shouldTransformAnswerDTOToAnswer() {
    Answer expectedAnswer = new Answer();
    Long questionId = 10L;
    expectedAnswer.setQuestionId(questionId);
    Long id = 1L;
    expectedAnswer.setId(id);
    String testAnswer = "test answer";
    expectedAnswer.setAnswer(testAnswer);
    User user = new User();
    String username = "username";
    user.setUserName(username);
    expectedAnswer.setAnsweredBy(user);

    Answer answer = answerTransformer.transformToAnswer(
        getAnswerDTO(questionId, id, testAnswer, username));

    assertThat(answer, samePropertyValuesAs(expectedAnswer));
  }

  @Test
  public void shouldTransformAnswerToAnswerDTOs() {
    Long questionId1 = 10L;
    Long id1 = 1L;
    String username1 = "username1";
    String testAnswer1 = "test answer1";

    Long questionId2 = 11L;
    Long id2 = 2L;
    String username2 = "username2";
    String testAnswer2 = "test answer2";

    List<AnswerDTO> answerDTO = answerTransformer.transformToAnswerDTOs(Arrays.asList(
        getAnswer(questionId1, id1, username1, testAnswer1),
        getAnswer(questionId2, id2, username2, testAnswer2)));

    assertThat(answerDTO, hasItems(
        getAnswerDTO(questionId1, id1, testAnswer1, username1),
        getAnswerDTO(questionId2, id2, testAnswer2, username2)
        ));
  }

  private Answer getAnswer(Long questionId1, Long id1, String username1, String testAnswer1) {
    Answer expectedAnswer = new Answer();
    expectedAnswer.setQuestionId(questionId1);
    expectedAnswer.setId(id1);
    expectedAnswer.setAnswer(testAnswer1);
    User user = new User();
    user.setUserName(username1);
    expectedAnswer.setAnsweredBy(user);
    return expectedAnswer;
  }

  private AnswerDTO getAnswerDTO(Long questionId, Long id, String testAnswer, String username) {
    return AnswerDTO.builder()
        .answer(testAnswer)
        .answeredBy(username)
        .id(id)
        .questionId(questionId)
        .build();
  }
}