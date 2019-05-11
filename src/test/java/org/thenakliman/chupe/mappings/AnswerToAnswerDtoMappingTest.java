package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class AnswerToAnswerDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() throws Exception {
    modelMapper.addMappings(new AnswerToAnswerDtoMapping().mapping());
  }

  @Test
  public void shouldMapAnswerToAnswerDTO() {
    String userName = "user-name";
    User user = User
        .builder()
        .userName(userName)
        .build();

    long answerId = 1001L;
    long questionId = 10L;
    String testAnswer = "test-Answer";
    Answer answer = Answer
        .builder()
        .answer(testAnswer)
        .answeredBy(user)
        .id(answerId)
        .question(Question.builder().id(questionId).build())
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    AnswerDTO answerDTO = modelMapper.map(answer, AnswerDTO.class);

    assertEquals(userName, answerDTO.getAnsweredBy());
    assertEquals(answerId, answerDTO.getId());
    assertEquals(questionId, answerDTO.getQuestionId());
    assertEquals(testAnswer, answerDTO.getAnswer());
  }
}