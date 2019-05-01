package org.thenakliman.chupe.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.config.TokenAuthenticationService;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.RetroPointService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = VoteController.class)
public class VoteControllerTest extends BaseControllerTest {
  private final String username = "username";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @MockBean
  private RetroPointService retroPointService;
  @MockBean
  private TokenService tokenService;
  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;
  @MockBean
  private ApplicationContext applicationContext;
  private Authentication authToken;

  /**
   * Setup web application context.
   */
  @Before()
  public void testSetup() {
    /* NOTE(thenakliman) this approach has been used to bypass authentication
     * layer from the test. It creates a standalone application with single
     * controller(TaskController).
     * */
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);
  }

  @Test
  public void shouldCasteVote() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    Long retroPointId = 9484L;

    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retro-point-votes/" + retroPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content((byte[]) null))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    verify(retroPointService).castVote(retroPointId, username);
  }

  @Test
  public void shouldThrowNotFoundException() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    Long retroPointId = 9484L;

    doThrow(new NotFoundException("")).when(retroPointService).castVote(retroPointId, username);
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retro-point-votes/" + retroPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content((byte[]) null))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

    verify(retroPointService).castVote(retroPointId, username);
  }

  @Test
  public void shouldThrowBadRequestException() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    Long retroPointId = 9484L;

    doThrow(new BadHttpRequest()).when(retroPointService).castVote(retroPointId, username);
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retro-point-votes/" + retroPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content((byte[]) null))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    verify(retroPointService).castVote(retroPointId, username);
  }
}