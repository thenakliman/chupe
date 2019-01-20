package org.thenakliman.chupe.transformer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;

import java.util.Date;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnswerDtoToAnswerMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new AnswerDtoToAnswerMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapAnswerToAnswerDTO() {
    String userName = "user-name";
    long answerId = 1001L;
    long questionId = 10L;
    String testAnswer = "test-Answer";

    AnswerDTO answerDTO = AnswerDTO
        .builder()
        .questionId(questionId)
        .id(answerId)
        .answeredBy(userName)
        .answer(testAnswer)
        .build();

    Answer answer = modelMapper.map(answerDTO, Answer.class);

    User user = User
        .builder()
        .userName(userName)
        .build();

    Answer expectedAnswer = Answer
        .builder()
        .answer(testAnswer)
        .answeredBy(user)
        .id(answerId)
        .questionId(questionId)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertThat(answer, samePropertyValuesAs(expectedAnswer));
  }
}