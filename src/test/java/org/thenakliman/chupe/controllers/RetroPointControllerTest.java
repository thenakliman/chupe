package org.thenakliman.chupe.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.config.TokenAuthenticationService;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.models.RetroPointType;
import org.thenakliman.chupe.services.RetroPointService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = RetroPointController.class)
public class RetroPointControllerTest extends BaseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private RetroPointService retroPointService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  @MockBean
  private ApplicationContext applicationContext;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private final Long retroId = 2357L;

  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    String username = "username";
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);
  }

  @Test
  public void shouldGetRetroPoints() throws Exception {
    RetroPointDTO retroPointDTO = getRetroPointDTO();
    given(retroPointService.getRetroPoints(retroId)).willReturn(singletonList(retroPointDTO));

    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/retro-points?retroId=" + retroId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    ArrayList result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), ArrayList.class);

    assertEquals(result.size(), 1);
  }

  private RetroPointDTO getRetroPointDTO() {
    return RetroPointDTO
        .builder()
        .id(101L)
        .addedBy("added by")
        .description("description")
        .retroId(retroId)
        .type(RetroPointType.DONE_WELL)
        .build();
  }

  @Test
  public void shouldCreateRetroPoint() throws Exception {
    RetroPointDTO retroDTO = getRetroPointDTO();
    given(retroPointService.saveRetroPoint(retroDTO)).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retro-points")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    RetroPointDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), RetroPointDTO.class);

    assertThat(result, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void shouldUpdateTask() throws Exception {
    RetroPointDTO retroDTO = getRetroPointDTO();
    long retroPointId = 10L;
    given(retroPointService.updateRetroPoint(retroPointId, retroDTO)).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retro-points/" + retroPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    RetroPointDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), RetroPointDTO.class);

    assertThat(result, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void shouldRaiseNotFoundWhenUpdateTask() throws Exception {
    RetroPointDTO retroDTO = getRetroPointDTO();
    long retroPointId = 10L;
    given(retroPointService.updateRetroPoint(retroPointId, retroDTO)).willThrow(
        new NotFoundException("not Found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroPointId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}