package org.thenakliman.chupe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.FeedbackPointService;
import org.thenakliman.chupe.services.FeedbackSessionService;
import org.thenakliman.chupe.services.TokenService;

import java.util.Collections;

import static java.lang.String.format;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = FeedbackPointController.class, secure = false)
@RunWith(SpringRunner.class)
public class FeedbackPointControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private FeedbackPointService feedbackPointService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private String username = "username";

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
  public void shouldAddFeedbackSession() throws Exception {
    UpsertFeedbackPointDTO feedbackPointDto = UpsertFeedbackPointDTO
        .builder()
        .description("feedback session description")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/feedback-points")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackPointDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    verify(feedbackPointService).saveFeedbackPoint(username, feedbackPointDto);
  }

  @Test
  public void shouldUpdateFeedbackSession() throws Exception {
    UpsertFeedbackPointDTO feedbackSession = UpsertFeedbackPointDTO
        .builder()
        .description("feedback session description")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    long feedbackPointId = 10101L;
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/feedback-points/" + feedbackPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

    verify(feedbackPointService).updateFeedbackPoint(username, feedbackPointId, feedbackSession);
  }

  @Test
  public void shouldGetFeedbackPointsGivenToAUser() throws Exception {
    FeedbackPointDTO feedbackSession = FeedbackPointDTO
        .builder()
        .id(10L)
        .description("feedback session description")
        .build();

    long sessionId = 10101L;
    SecurityContextHolder.getContext().setAuthentication(authToken);
    when(feedbackPointService.getFeedbackPointsGivenToUser(username, sessionId))
        .thenReturn(Collections.singletonList(feedbackSession));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/feedback-points?feedbackSessionId=" + sessionId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    verify(feedbackPointService).getFeedbackPointsGivenToUser(username, sessionId);
  }

  @Test
  public void shouldGetFeedbackPointsGivenToAUserByAUser() throws Exception {
    FeedbackPointDTO feedbackSession = FeedbackPointDTO
        .builder()
        .id(10L)
        .description("feedback session description")
        .build();

    SecurityContextHolder.getContext().setAuthentication(authToken);
    long sessionId = 10101L;
    String givenTo = "username2";
    when(feedbackPointService.getFeedbackGivenToUserByAUser(username, givenTo, sessionId))
        .thenReturn(Collections.singletonList(feedbackSession));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get(format("/api/v1/feedback-points?givenTo=%s&feedbackSessionId=%s", givenTo, sessionId))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    verify(feedbackPointService).getFeedbackGivenToUserByAUser(username, givenTo, sessionId);
  }
}