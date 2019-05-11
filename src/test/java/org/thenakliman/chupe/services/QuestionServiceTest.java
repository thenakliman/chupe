package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.QuestionRepository;


@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {
  static final long id = 100;
  @Mock
  private QuestionRepository questionsRepository;
  @Mock
  private Converter converter;
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private QuestionService questionService;

  private Question getTestQuestion() {
    Question question = new Question();
    question.setQuestion("What is your name?");
    question.setAssignedTo(User.builder().userName("testUser1").build());
    question.setDescription("Need your name for auth service");
    question.setOwner(User.builder().userName("testUser2").build());
    question.setId(id);
    question.setPriority(QuestionPriority.LOW);
    question.setStatus(QuestionStatus.OPEN);
    return question;
  }

  private UpsertQuestionDTO getTestUpsertQuestionDto() {
    return UpsertQuestionDTO
        .builder()
        .question("What is your name?")
        .assignedTo("testUser1")
        .description("Need your name for auth service")
        .priority(QuestionPriority.LOW)
        .status(QuestionStatus.OPEN)
        .build();
  }

  private QuestionDTO getTestQuestionDTO() {
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setQuestion("What is your name?");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setOwner("testUser2");
    questionDTO.setId(id);
    questionDTO.setStatus(QuestionStatus.OPEN);
    questionDTO.setPriority(QuestionPriority.LOW);
    return questionDTO;
  }

  @Test
  public void shouldCreateQuestion() {
    Question question = getTestQuestion();
    QuestionDTO questionDTO = getTestQuestionDTO();
    UpsertQuestionDTO upsertQuestionDTO = getTestUpsertQuestionDto();
    given(converter.convertToObject(upsertQuestionDTO, Question.class)).willReturn(question);
    given(questionsRepository.save(question)).willReturn(question);
    given(converter.convertToObject(question, QuestionDTO.class)).willReturn(questionDTO);

    QuestionDTO receivedQuestion = questionService.addQuestion(upsertQuestionDTO, "testUser2");

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
    given(converter.convertToListOfObjects(questions, QuestionDTO.class)).willReturn(questionDTOs);

    List<QuestionDTO> receivedQuestion = questionService.getQuestions();

    assertThat(questionDTOs, samePropertyValuesAs(receivedQuestion));
  }

  @Test
  public void shouldUpdateQuestions() throws NotFoundException {
    Question question = getTestQuestion();
    QuestionDTO questionDTO = getTestQuestionDTO();
    question.setDescription(questionDTO.getDescription());
    question.setAssignedTo(User.builder().userName(questionDTO.getAssignedTo()).build());
    question.setOwner(User.builder().userName(questionDTO.getOwner()).build());
    question.setQuestion(questionDTO.getQuestion());
    Date now = new Date();
    question.setUpdatedAt(now);
    when(dateUtil.getTime()).thenReturn(now);
    when(questionsRepository.findByIdAndOwnerUserName(id, "testUser2")).thenReturn(Optional.of(getTestQuestion()));
    question.setUpdatedAt(now);
    when(converter.convertToObject(question, QuestionDTO.class)).thenReturn(questionDTO);
    when(questionsRepository.save(question)).thenReturn(question);

    QuestionDTO updatedQuestion = questionService.updateQuestions(id, getTestUpsertQuestionDto(), "testUser2");

    assertThat(updatedQuestion, samePropertyValuesAs(questionDTO));
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaiseNotFoundException() throws NotFoundException {
    when(questionsRepository.findByIdAndOwnerUserName(id, "testUser2")).thenReturn(Optional.empty());
    questionService.updateQuestions(id, getTestUpsertQuestionDto(), "testUser2");
  }
}