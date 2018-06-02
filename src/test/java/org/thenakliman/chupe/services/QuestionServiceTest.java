package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
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

  @Captor
  ArgumentCaptor<Question> captor;

  static final long id = 100;

  private Question getTestQuestion() {
    Question question = new Question();
    question.setQuestion("What is your name?");
    question.setAssignedTo("testUser1");
    question.setDescription("Need your name for auth service");
    question.setOwner("testUser2");
    question.setId(id);
    return question;
  }

  private QuestionDTO getTestQuestionDTO() {
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setQuestion("question dto");
    questionDTO.setAssignedTo("assignedto dto");
    questionDTO.setDescription("description dto");
    questionDTO.setOwner("testUser2");
    questionDTO.setId(id);
    return questionDTO;
  }

  @Test
  public void shouldCreateQuestion() {
    Question question = getTestQuestion();
    QuestionDTO questionDTO = getTestQuestionDTO();
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
    Question question1 = getTestQuestion();
    List<Question> questions = new ArrayList<>();
    questions.add(question1);
    BDDMockito.given(questionsRepository.findAll()).willReturn(questions);

    QuestionDTO questionDTO = getTestQuestionDTO();
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    questionDTOs.add(questionDTO);
    BDDMockito.given(questionTransformer.transformToQuestionDTO(questions))
            .willReturn(questionDTOs);
    List<QuestionDTO> receivedQuestion = questionService.getQuestions();

    assertThat(questionDTOs, samePropertyValuesAs(receivedQuestion));
  }

  @Test
  public void shouldUpdateQuestions() throws NotFoundException {
    Question updatedQuestion = getTestQuestion();
    QuestionDTO questionDTO = getTestQuestionDTO();
    updatedQuestion.setDescription(questionDTO.getDescription());
    updatedQuestion.setAssignedTo(questionDTO.getAssignedTo());
    updatedQuestion.setOwner(questionDTO.getOwner());
    updatedQuestion.setQuestion(questionDTO.getQuestion());

    when(questionsRepository.findById(id)).thenReturn(Optional.of(getTestQuestion()));

    questionService.updateQuestions(id, getTestQuestionDTO());

    verify(questionsRepository).save(captor.capture());
    assertThat(updatedQuestion, samePropertyValuesAs(captor.getValue()));
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaiseNotFoundException() throws NotFoundException {
    when(questionsRepository.findById(id)).thenReturn(null);
    questionService.updateQuestions(id, getTestQuestionDTO());
  }
}