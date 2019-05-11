package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class QuestionToQuestionDtoMappingTest {
  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() throws Exception {
    modelMapper.addMappings(new QuestionToQuestionDtoMapping().mapping());
  }

  @Test
  public void shouldMapQuestionToQuestionDTO() {
    String userName = "user-name";
    User assignedTo = User
        .builder()
        .userName(userName)
        .build();

    User owner = User
        .builder()
        .userName("username - 2")
        .build();

    long questionId = 10L;
    String testQuestion = "test-Question";
    Date date = new Date();
    Question answer = Question
        .builder()
        .assignedTo(assignedTo)
        .question(testQuestion)
        .status(QuestionStatus.OPEN)
        .priority(QuestionPriority.HIGH)
        .description("description - 3")
        .owner(owner)
        .id(questionId)
        .createdAt(date)
        .updatedAt(date)
        .build();

    QuestionDTO questionDTO = modelMapper.map(answer, QuestionDTO.class);

    assertEquals(userName, questionDTO.getAssignedTo());
    assertEquals("username - 2", questionDTO.getOwner());
    assertEquals(questionId, questionDTO.getId());
    assertEquals(questionId, questionDTO.getId());
    assertEquals(testQuestion, questionDTO.getQuestion());
    assertEquals(QuestionStatus.OPEN, questionDTO.getStatus());
    assertEquals(QuestionPriority.HIGH, questionDTO.getPriority());
  }
}