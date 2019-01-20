package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.config.TokenAuthenticationService;
import org.thenakliman.chupe.dto.AnswerDTO;
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

  /** Setup web application context. */
  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    /* NOTE(thenakliman) this approach has been used to bypass authentication
     * layer from the test. It creates a standalone application with single
     * controller(QuestionController).
     * */
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldGetAnswerToGivenQuestion() throws  Exception {
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
  public void shouldReturnNotFoundStatusCodeIfQuestionNotFound() throws  Exception {
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
  public void shouldCreateAnswer() throws  Exception {
    int questionId = 100;
    String user = "user";
    String answer1 = "answer";

    AnswerDTO expectedAnswer = getAnswerDTO(questionId, user, answer1);
    AnswerDTO answer = getAnswerDTO(questionId, user, answer1);

    BDDMockito.given(answerService.addAnswer(any())).willReturn(expectedAnswer);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(answer)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    AnswerDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), AnswerDTO.class);

    assertThat(expectedAnswer, samePropertyValuesAs(result));
  }

  private AnswerDTO getAnswerDTO(int questionId, String user, String answer1) {
    return AnswerDTO.builder()
        .questionId(questionId)
        .answeredBy(user)
        .answer(answer1).build();
  }

  @Test
  public void shouldReturnNotFoundStatusCodeIfAnswerNotFound() throws  Exception {
    Long answerId = 1000L;
    AnswerDTO answerDTO = AnswerDTO.builder().build();
    BDDMockito.given(
        answerService.updateAnswer(answerId, answerDTO))
        .willThrow(new NotFoundException(answerId + " question not found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/answers/" + answerId)
        .content(objectMapper.writeValueAsString(answerDTO))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void shouldReturnUpdatedAnswer() throws  Exception {
    Long answerId = 1000L;
    AnswerDTO answer = getAnswerDTO(10, "user", "answer");
    BDDMockito.given(
        answerService.updateAnswer(answerId, answer))
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
}