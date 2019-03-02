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
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.User;
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
//    assertThat(result, hasItems(retroDTO));
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

  @Test
  public void shouldCreateRetro() throws  Exception {
    String name = "today name";
    RetroDTO retroDTO = getRetroDTO(name);
    given(retroService.saveRetro(retroDTO)).willReturn(retroDTO);

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
  public void shouldUpdateTask() throws  Exception {
    String name = "today task";
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, retroDTO)).willReturn(retroDTO);

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
  public void shouldRaiseNotFoundWhenUpdateTask() throws  Exception {
    String name = "my name";
    RetroDTO retroDTO = getRetroDTO(name);
    long retroId = 10L;
    given(retroService.updateRetro(retroId, retroDTO)).willThrow(
        new NotFoundException("not Found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retros/" + retroId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(retroDTO)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}