package org.thenakliman.chupe.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

  @Mock
  private QuestionRepository questionsRepository;

  @InjectMocks
  private QuestionService questionService;

  @Test
  public void shouldCreateQuestion() {
    Question question = new Question();
    question.setQuestion("What is your name?");
    question.setAssignedTo("testUser1");
    question.setDescription("Need your name for auth service");
    question.setOwner("testUser2");
    question.setId(100);
    BDDMockito.given(questionsRepository.save(question)).willReturn(question);

    Question receivedQuestion = questionService.addQuestion(question);
    assertEquals(question, receivedQuestion);
  }

}