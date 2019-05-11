package org.thenakliman.chupe.mappings;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertAnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class UpsertAnswerDtoToAnswerMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new AnswerDtoToAnswerMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapAnswerToUpsertAnswerDTO() {
    String userName = "user-name";
    long answerId = 1001L;
    long questionId = 10L;
    String testAnswer = "test-Answer";

    UpsertAnswerDTO answerDTO = UpsertAnswerDTO
        .builder()
        .questionId(questionId)
        .answer(testAnswer)
        .build();

    Answer answer = modelMapper.map(answerDTO, Answer.class);

    Answer expectedAnswer = Answer
        .builder()
        .answer(testAnswer)
        .answeredBy(null)
        .id(null)
        .question(Question.builder().id(questionId).build())
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertThat(answer, samePropertyValuesAs(expectedAnswer));
  }
}