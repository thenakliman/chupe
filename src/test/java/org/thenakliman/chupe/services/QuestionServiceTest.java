package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.repositories.QuestionRepository;


@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {
  static final long id = 100;
  @Captor
  ArgumentCaptor<Question> captor;
  @Mock
  private QuestionRepository questionsRepository;
  @Mock
  private ModelMapper modelMapper;
  @InjectMocks
  private QuestionService questionService;

  private Question getTestQuestion() {
    Question question = new Question();
    question.setQuestion("What is your name?");
    question.setAssignedTo("testUser1");
    question.setDescription("Need your name for auth service");
    question.setOwner("testUser2");
    question.setId(id);
    question.setPriority(QuestionPriority.LOW);
    question.setStatus(QuestionStatus.OPEN);
    return question;
  }

  private QuestionDTO getTestQuestionDTO() {
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setQuestion("question dto");
    questionDTO.setAssignedTo("assignedto dto");
    questionDTO.setDescription("description dto");
    questionDTO.setOwner("testUser2");
    questionDTO.setId(id);
    questionDTO.setStatus(QuestionStatus.OPEN);
    questionDTO.setPriority(QuestionPriority.LOW);
    return questionDTO;
  }

  @Test
  public void shouldCreateQuestion() {
    Question question = getTestQuestion();
    given(questionsRepository.save(question)).willReturn(question);
    QuestionDTO questionDTO = getTestQuestionDTO();
    given(modelMapper.map(question, QuestionDTO.class)).willReturn(questionDTO);

    QuestionDTO receivedQuestion = questionService.addQuestion(question);

    assertEquals(questionDTO, receivedQuestion);
  }

  @Test
  public void shouldReturnNullIfNoQuestion() {
    List<Question> question = new ArrayList<>();
    given(questionsRepository.findAll()).willReturn(question);

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
    given(questionsRepository.findAll()).willReturn(questions);

    QuestionDTO questionDTO = getTestQuestionDTO();
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    questionDTOs.add(questionDTO);
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
    when(questionsRepository.findById(id)).thenReturn(Optional.empty());
    questionService.updateQuestions(id, getTestQuestionDTO());
  }
}