package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;
import org.thenakliman.chupe.transformer.QuestionTransformer;


@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {
  @Mock
  private QuestionTransformer questionTransformer;

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

    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setQuestion("What is your name?");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setOwner("testUser2");
    questionDTO.setId(100);
    BDDMockito.given(questionsRepository.save(question)).willReturn(question);
    BDDMockito.given(questionTransformer.transformToQuestionDTO(question))
            .willReturn(questionDTO);

    QuestionDTO receivedQuestion = questionService.addQuestion(question);
    assertEquals(questionDTO, receivedQuestion);
  }

  @Test
  public void shouldReturnNullIfNoQuestion() {
    List<Question> question = new ArrayList<>();
    BDDMockito.given(questionsRepository.findAll()).willReturn(question);
    BDDMockito.given(questionTransformer.transformToQuestionDTO(question))
            .willReturn(new ArrayList<>());
    List<QuestionDTO> receivedQuestion = questionService.getQuestions();
    assertEquals(new ArrayList<>(), receivedQuestion);
  }

  @Test
  public void shouldReturnQuestions() {
    String questionString = "What is your name?";
    String assignedUser = "testUser1";
    String description = "Need your name for auth service";
    String owner = "testUser2";
    int id = 100;
    Question question1 = new Question();
    question1.setQuestion(questionString);
    question1.setAssignedTo(assignedUser);
    question1.setDescription(description);
    question1.setOwner(owner);
    question1.setId(id);
    List<Question> questions = new ArrayList<>();
    questions.add(question1);
    BDDMockito.given(questionsRepository.findAll()).willReturn(questions);

    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setId(id);
    questionDTO.setQuestion(questionString);
    questionDTO.setAssignedTo(assignedUser);
    questionDTO.setOwner(owner);
    questionDTO.setDescription(description);
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    questionDTOs.add(questionDTO);
    BDDMockito.given(questionTransformer.transformToQuestionDTO(questions))
            .willReturn(questionDTOs);
    List<QuestionDTO> receivedQuestion = questionService.getQuestions();

    assertThat(questionDTOs, samePropertyValuesAs(receivedQuestion));
  }
}