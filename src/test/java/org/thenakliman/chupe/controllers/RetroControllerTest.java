package org.thenakliman.chupe.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpdateRetroStatusDto;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.services.RetroService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = RetroController.class)
public class RetroControllerTest extends BaseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private RetroService retroService;

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
  public void shouldGetRetros() throws Exception {
    String name = "my - name";
    RetroDTO retroDTO = getRetroDTO(name);

    given(retroService.getRetros()).willReturn(singletonList(retroDTO));

    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/retros")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    ArrayList result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), ArrayList.class);

    assertEquals(result.size(), 1);
  }

  private RetroDTO getRetroDTO(String name) {
    return RetroDTO
        .builder()
        .name(name)
        .id(10L)
        .createdBy("username")
        .maximumVote(10L)
        .build();
  }

  private UpsertRetroDTO getUpsertRetroDTO(String name) {
    return UpsertRetroDTO
        .builder()
        .name(name)
        .maximumVote(10L)
        .build();
  }

  @Test
  public void shouldCreateRetro() throws Exception {
    String name = "today name";
    RetroDTO retroDTO = getRetroDTO(name);
    given(retroService.saveRetro(any(), any())).willReturn(retroDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retros")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    RetroDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), RetroDTO.class);

    assertThat(result, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void shouldReturnBadRequestWhenRetroNameLengthIs9() throws Exception {
    String name = "today1234";
    RetroDTO retroDTO = getRetroDTO(name);
    given(retroService.saveRetro(any(), any())).willReturn(retroDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retros")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenRetroNameLengthIs257() throws Exception {
    RetroDTO retroDTO = getRetroDTO(getStringWithLength(257));
    given(retroService.saveRetro(any(), any())).willReturn(retroDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retros")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenRetroVoteIs0() throws Exception {
    RetroDTO retroDTO = getRetroDTO("a valid retro name");
    retroDTO.setMaximumVote(0L);
    given(retroService.saveRetro(any(), any())).willReturn(retroDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retros")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  private String getStringWithLength(int length) {
    return IntStream
        .range(0, length)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(""));
  }

  @Test
  public void shouldUpdateTask() throws Exception {
    String name = "today task";
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, getUpsertRetroDTO(name))).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    RetroDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), RetroDTO.class);

    assertThat(result, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void shouldUpdateStatus() throws Exception {
    UpdateRetroStatusDto updateRetroStatusDto = new UpdateRetroStatusDto(RetroStatus.IN_PROGRESS);
    long retroId = 10L;

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retro-status/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRetroStatusDto)))
        .andExpect(MockMvcResultMatchers.status().isNoContent() ).andReturn();

    verify(retroService).changeRetroStatus(retroId, updateRetroStatusDto);
  }

  @Test
  public void shouldReturnBadRequestWhenUpdateTaskWhenRetroNameLengthIs9() throws Exception {
    String name = "today tas";
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, getUpsertRetroDTO(name))).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenUpdateTaskWhenRetroNameLengthIs257() throws Exception {
    String name = getStringWithLength(257);
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, getUpsertRetroDTO(name))).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnBadRequestWhenUpdateTaskWhenRetroVoteIs0() throws Exception {
    String name = getStringWithLength(257);
    RetroDTO retroDTO = getRetroDTO(name);
    retroDTO.setMaximumVote(0L);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, getUpsertRetroDTO(name))).willReturn(retroDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldRaiseNotFoundWhenUpdateTask() throws Exception {
    String name = "my name is a valid name";
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, getUpsertRetroDTO(name))).willThrow(
        new NotFoundException("not Found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}