package org.thenakliman.chupe.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.config.TokenAuthenticationService;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.dto.UpsertAnswerDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.services.AnswerService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = AnswerController.class)
@RunWith(SpringRunner.class)
public class AnswerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private AnswerService answerService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private String username = "user-name";

  /**
   * Setup web application context.
   */
  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    /* NOTE(thenakliman) this approach has been used to bypass authentication
     * layer from the test. It creates a standalone application with single
     * controller(QuestionController).
     * */
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);
  }

  @Test
  public void shouldGetAnswerToGivenQuestion() throws Exception {
    int questionId = 100;
    AnswerDTO answer = getAnswerDTO(questionId, "user", "answer");
    answer.setId(10L);
    List<AnswerDTO> answers = new ArrayList<AnswerDTO>();
    answers.add(answer);
    BDDMockito.given(answerService.getAnswers(questionId)).willReturn(answers);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/answers?questionId=" + questionId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<Answer> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), List.class);

    assertThat(answers, samePropertyValuesAs(result));
  }

  @Test
  public void shouldReturnNotFoundStatusCodeIfQuestionNotFound() throws Exception {
    int questionId = 1000;
    BDDMockito.given(
        answerService.getAnswers(questionId))
        .willThrow(new NotFoundException(questionId + " question not found"));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/answers?questionId=" + questionId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void shouldCreateAnswer() throws Exception {
    int questionId = 100;
    String user = "user";
    String answer1 = "answer hello";

    AnswerDTO expectedAnswer = getAnswerDTO(questionId, user, answer1);
    AnswerDTO answer = getAnswerDTO(questionId, user, answer1);

    BDDMockito.given(answerService.addAnswer(any(), anyString())).willReturn(expectedAnswer);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(answer)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    AnswerDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), AnswerDTO.class);

    assertThat(expectedAnswer, samePropertyValuesAs(result));
  }

  @Test
  public void shouldGiveBadRequestWhenAnswerLengthIs8() throws Exception {
    int questionId = 100;
    String user = "user";
    String answer1 = "answer12";

    AnswerDTO expectedAnswer = getAnswerDTO(questionId, user, answer1);
    AnswerDTO answer = getAnswerDTO(questionId, user, answer1);

    BDDMockito.given(answerService.addAnswer(any(), anyString())).willReturn(expectedAnswer);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(answer)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGiveBadRequestWhenAnswerLengthIs1001() throws Exception {
    int questionId = 100;
    String user = "user";
    String answer1 = getStringWithLength(1001);

    AnswerDTO expectedAnswer = getAnswerDTO(questionId, user, answer1);
    AnswerDTO answer = getAnswerDTO(questionId, user, answer1);

    BDDMockito.given(answerService.addAnswer(any(), anyString())).willReturn(expectedAnswer);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(answer)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  private AnswerDTO getAnswerDTO(int questionId, String user, String answer1) {
    return AnswerDTO.builder()
        .questionId(questionId)
        .answeredBy(user)
        .answer(answer1).build();
  }

  @Test
  public void shouldReturnNotFoundStatusCodeIfAnswerNotFound() throws Exception {
    Long answerId = 1000L;
    SecurityContextHolder.getContext().setAuthentication(authToken);

    BDDMockito.given(
        answerService.updateAnswer(answerId, UpsertAnswerDTO.builder().build(), username))
        .willThrow(new NotFoundException(answerId + " question not found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/answers/" + answerId)
        .content(objectMapper.writeValueAsString(AnswerDTO.builder().build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void shouldReturnUpdatedAnswer() throws Exception {
    Long answerId = 1000L;
    AnswerDTO answer = getAnswerDTO(10, "user", "answer hello");
    UpsertAnswerDTO upsertAnswerDTO = UpsertAnswerDTO
        .builder()
        .questionId(10)
        .answer("answer hello")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);

    BDDMockito.given(
        answerService.updateAnswer(answerId, upsertAnswerDTO, username))
        .willReturn(answer);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/answers/" + answerId)
        .content(objectMapper.writeValueAsString(answer))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    AnswerDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), AnswerDTO.class);

    assertThat(answer, samePropertyValuesAs(result));

  }

  @Test
  public void shouldReturnBadRequestWhenAnswerIsLessThan10() throws Exception {
    Long answerId = 1000L;
    AnswerDTO answer = getAnswerDTO(10, "user", "answer");
    UpsertAnswerDTO upsertAnswerDTO = UpsertAnswerDTO.builder().questionId(10).answer("answer").build();
    SecurityContextHolder.getContext().setAuthentication(authToken);

    BDDMockito.given(
        answerService.updateAnswer(answerId, upsertAnswerDTO, username))
        .willReturn(answer);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/answers/" + answerId)
        .content(objectMapper.writeValueAsString(answer))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenAnswerIs1001() throws Exception {
    Long answerId = 1000L;
    AnswerDTO answer = getAnswerDTO(10, "user", getStringWithLength(1001));
    UpsertAnswerDTO upsertAnswerDTO = UpsertAnswerDTO.builder().questionId(10).answer("answer").build();
    SecurityContextHolder.getContext().setAuthentication(authToken);

    BDDMockito.given(
        answerService.updateAnswer(answerId, upsertAnswerDTO, username))
        .willReturn(answer);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/answers/" + answerId)
        .content(objectMapper.writeValueAsString(answer))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  private String getStringWithLength(int length) {
    return IntStream
        .range(0, length)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(""));
  }
}