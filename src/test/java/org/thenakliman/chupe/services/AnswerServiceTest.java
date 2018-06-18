package org.thenakliman.chupe.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.repositories.AnswerRepository;

@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {

  @Mock
  AnswerRepository answerRepository;

  @InjectMocks
  AnswerService answerService;

  @Test
  public void shouldReturnAllAnswerOfGivenQuestion() throws NotFoundException {
    Answer answer = new Answer();
    answer.setAnswer("testAnswer");
    answer.setAnsweredBy("user");
    int questionId = 10;
    answer.setId(1);
    answer.setQuestionId(questionId);

    List<Answer> answers = new ArrayList<>();
    answers.add(answer);

    BDDMockito.given(answerRepository.findByQuestionId(questionId)).willReturn(answers);

    List<Answer> receivedAnswer = answerService.getAnswersOfGivenQuestion(questionId);

    assertEquals(receivedAnswer, answers);
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionIfAnswerDoesNotExistForAQuestion()
        throws NotFoundException {
    int questionId = 10;
    BDDMockito.given(answerRepository.findByQuestionId(questionId)).willReturn(null);
    answerService.getAnswersOfGivenQuestion(questionId);
  }

}