package org.thenakliman.chupe.controllers;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

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
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.BestPracticeService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = BestPracticesController.class, secure = false)
@RunWith(SpringRunner.class)
public class BestPracticesControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private BestPracticeService bestPracticeService;

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
  public void shouldGetBestPractice() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BestPracticeDTO bestPracticeDTO = BestPracticeDTO.builder()
        .id(101L)
        .description("description")
        .build();

    when(bestPracticeService.getActiveBestPractices()).thenReturn(Collections.singletonList(bestPracticeDTO));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/best-practices")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    verify(bestPracticeService).getActiveBestPractices();
    BestPracticeDTO[] bestPracticeDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeDTO[].class);
    assertThat(bestPracticeDTOs, arrayWithSize(1));
    assertThat(bestPracticeDTOs, hasItemInArray(bestPracticeDTO));
  }

  @Test
  public void shouldCreateBestPractice() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    UpsertBestPracticeDTO upsertBestPracticeDTO = UpsertBestPracticeDTO.builder()
        .description("description")
        .applicable(true)
        .build();

    BestPracticeDTO bestPracticeDTO = BestPracticeDTO.builder()
        .description("description")
        .build();

    when(bestPracticeService.saveBestPractice(upsertBestPracticeDTO, "username")).thenReturn(bestPracticeDTO);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/best-practices")
        .content(objectMapper.writeValueAsBytes(upsertBestPracticeDTO))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    verify(bestPracticeService).saveBestPractice(upsertBestPracticeDTO, "username");
    BestPracticeDTO actualBestPracticeDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeDTO.class);
    assertThat(actualBestPracticeDTO, samePropertyValuesAs(bestPracticeDTO));
  }
}