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
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class UpsertUpsertQuestionDtoToQuestionMappingTest {
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new UpsertQuestionDtoToQuestionMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapQuestionToUpsertQuestionDTO() {
    String userName = "user-name";
    long answerId = 1001L;
    long questionId = 10L;
    String testQuestion = "test-Question";

    String description = "description - 1";
    UpsertQuestionDTO answerDTO = UpsertQuestionDTO
        .builder()
        .status(QuestionStatus.OPEN)
        .priority(QuestionPriority.LOW)
        .description(description)
        .question(testQuestion)
        .assignedTo(userName)
        .build();

    Question answer = modelMapper.map(answerDTO, Question.class);

    Question expectedQuestion = Question
        .builder()
        .status(QuestionStatus.OPEN)
        .assignedTo(User.builder().userName(userName).build())
        .description(description)
        .question(testQuestion)
        .priority(QuestionPriority.LOW)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertThat(answer, samePropertyValuesAs(expectedQuestion));
  }
}