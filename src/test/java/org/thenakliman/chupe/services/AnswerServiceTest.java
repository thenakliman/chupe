package org.thenakliman.chupe.services;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.given;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.AnswerRepository;

@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {
  @Mock
  private ModelMapper modelMapper;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private org.thenakliman.chupe.common.utils.DateUtil dateUtil;

  @InjectMocks
  private AnswerService answerService;

  private AnswerDTO getAnswerDTO(String username, String answer, Long questionId, Long id) {
    return AnswerDTO.builder()
        .answer(username)
        .answer(answer)
        .questionId(questionId)
        .id(id)
        .build();
  }

  private Answer getAnswer(String username, String answer, Long questionId, Long id) {
    Answer answerModel = new Answer();
    answerModel.setAnswer(answer);
    User user = new User();
    user.setUserName(username);
    answerModel.setAnsweredBy(user);
    answerModel.setId(id);
    answerModel.setQuestionId(questionId);
    return answerModel;
  }

  @Test
  public void shouldReturnAllAnswerOfGivenQuestion() throws NotFoundException {
    String username = "user";
    String testAnswer = "testAnswer";
    Long questionId = 10L;
    Long answerId = 1L;
    Answer answer = getAnswer(username, testAnswer, questionId, answerId);
    given(answerRepository.findByQuestionId(questionId)).willReturn(singletonList(answer));
    AnswerDTO answerDTO = getAnswerDTO(username, testAnswer, questionId, answerId);
    given(modelMapper.map(answer, AnswerDTO.class)).willReturn(answerDTO);

    List<AnswerDTO> receivedAnswer = answerService.getAnswers(questionId);

    assertThat(receivedAnswer, hasSize(1));
    assertThat(receivedAnswer, hasItems(getAnswerDTO(username, testAnswer, questionId, answerId)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionIfAnswerDoesNotExistForAQuestion()
      throws NotFoundException {
    int questionId = 10;
    given(answerRepository.findByQuestionId(questionId)).willReturn(null);
    answerService.getAnswers(questionId);
  }

  @Test
  public void shouldReturnAnswerAfterSave() {
    Long questionId = 10L;
    Long id = 1011L;
    String testAnswer = "test answer";
    String user = "user";
    AnswerDTO answerDTO = getAnswerDTO(testAnswer, user, questionId, id);
    Answer answer = getAnswer(testAnswer, user, questionId, id);

    given(modelMapper.map(answerDTO, Answer.class)).willReturn(answer);
    given(answerRepository.save(answer)).willReturn(answer);
    given(modelMapper.map(answer, AnswerDTO.class)).willReturn(answerDTO);

    assertThat(answerService.addAnswer(answerDTO), samePropertyValuesAs(answerDTO));
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionIfAnswerDoesNotExistForAQuestionWhileUpating()
      throws NotFoundException {
    Long answerId = 10L;
    given(answerRepository.findById(answerId)).willReturn(Optional.empty());
    answerService.updateAnswer(answerId, AnswerDTO.builder().build());
  }

  @Test
  public void shouldReturnUpdatedAnswerAfterUpdate() throws NotFoundException {
    Date date = DateUtil.now();
    String testAnswer = "testAnswer";
    String user = "user";
    Long questionId = 10L;
    Long answerId = 1011L;


    given(dateUtil.getTime()).willReturn(date);
    AnswerDTO answerDTO = getAnswerDTO(testAnswer, user, questionId, answerId);
    Answer answer = getAnswer(testAnswer, user, questionId, answerId);
    given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
    given(answerRepository.save(answer)).willReturn(answer);
    given(modelMapper.map(answer, AnswerDTO.class)).willReturn(answerDTO);

    assertThat(answerDTO, samePropertyValuesAs(answerService.updateAnswer(answerId, answerDTO)));
  }
}
