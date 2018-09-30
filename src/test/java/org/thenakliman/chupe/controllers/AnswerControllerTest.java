package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    Answer answer = new Answer();
    int questionId = 100;
    answer.setQuestionId(questionId);
    answer.setAnsweredBy("user");
    answer.setId(10);
    answer.setAnswer("answer");
    List<Answer> answers = new ArrayList<Answer>();
    answers.add(answer);
    BDDMockito.given(answerService.getAnswersOfGivenQuestion(questionId)).willReturn(answers);

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
        answerService.getAnswersOfGivenQuestion(questionId))
            .willThrow(new NotFoundException(questionId + " question not found"));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/answers?questionId=" + questionId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void shouldCreateAnswer() throws  Exception {
    Answer answer = new Answer();
    int questionId = 100;
    answer.setQuestionId(questionId);
    String user = "user";
    answer.setAnsweredBy(user);
    String answer1 = "answer";
    answer.setAnswer(answer1);

    Answer expectedAnswer = new Answer();
    expectedAnswer.setQuestionId(questionId);
    expectedAnswer.setAnsweredBy(user);
    expectedAnswer.setId(10);
    expectedAnswer.setAnswer(answer1);

    BDDMockito.given(answerService.addAnswer(any())).willReturn(expectedAnswer);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(answer)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    Answer result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), Answer.class);

    assertThat(expectedAnswer, samePropertyValuesAs(result));
  }
}