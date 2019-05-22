package org.thenakliman.chupe.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.FeedbackSessionService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = FeedbackSessionController.class, secure = false)
@RunWith(SpringRunner.class)
public class FeedbackSessionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private FeedbackSessionService feedbackSessionService;

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
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description("feedback session description")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/feedback-sessions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    verify(feedbackSessionService).createSession(feedbackSession, username);
  }

  @Test
  public void shouldAddFeedbackSessionThrowsBadRequestWhenDescriptionIsLessThan10() throws Exception {
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description("feed")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/feedback-sessions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldAddFeedbackSessionThrowsBadRequestWhenDescriptionIsGreaterThan256() throws Exception {
    String description = getStringWith257Length();

    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description(description)
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/feedback-sessions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  private String getStringWith257Length() {
    return IntStream
        .range(0, 257)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(""));
  }

  @Test
  public void shouldUpdateFeedbackSession() throws Exception {
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description("feedback session description")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    long sessionId = 10101L;
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/feedback-sessions/" + sessionId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

    verify(feedbackSessionService).updateSession(sessionId, feedbackSession, username);
  }

  @Test
  public void shouldUpdateReturnBadRequestFeedbackSessionWhenDescriptionIsLessThan10() throws Exception {
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description("feedback")
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    long sessionId = 10101L;
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/feedback-sessions/" + sessionId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenDescriptionLengthIs257() throws Exception {
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description(getStringWith257Length())
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    long sessionId = 10101L;
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/feedback-sessions/" + sessionId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenSessionIdIsNull() throws Exception {
    UpsertFeedbackSessionDTO feedbackSession = UpsertFeedbackSessionDTO
        .builder()
        .description(getStringWith257Length())
        .build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/feedback-sessions/" + null)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(feedbackSession)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGetFeedbackSession() throws Exception {
    FeedbackSessionDTO feedbackSession = FeedbackSessionDTO
        .builder()
        .id(10L)
        .description("feedback session description")
        .build();

    SecurityContextHolder.getContext().setAuthentication(authToken);
    when(feedbackSessionService.getFeedbackSessions(username))
        .thenReturn(Collections.singletonList(feedbackSession));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/feedback-sessions")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    verify(feedbackSessionService).getFeedbackSessions(username);
  }
}