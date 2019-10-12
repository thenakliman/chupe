package org.thenakliman.chupe.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.services.BestPracticeAssessmentService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = BestPracticeAssessmentController.class, secure = false)
@RunWith(SpringRunner.class)
public class BestPracticeAssessmentControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private BestPracticeAssessmentService bestPracticeAssessmentService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private String username = "username";

  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);

  }

  @Test
  public void shouldGetBestPractice() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    UpsertBestPracticeAssessmentDTO upsertBestPracticeAssessmentDTO = UpsertBestPracticeAssessmentDTO.builder()
        .bestPracticeId(100L)
        .answer(false)
        .retroId(102L)
        .build();

    BestPracticeAssessmentDTO bestPracticeAssessmentDTO = BestPracticeAssessmentDTO.builder()
        .bestPracticeId(100L)
        .answer(false)
        .retroId(102L)
        .id(1222L)
        .build();

    when(bestPracticeAssessmentService.saveBestPracticeAssessment(singletonList(upsertBestPracticeAssessmentDTO), username))
        .thenReturn(singletonList(bestPracticeAssessmentDTO));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/best-practices-assessments")
        .content(objectMapper.writeValueAsBytes(singletonList(upsertBestPracticeAssessmentDTO)))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    verify(bestPracticeAssessmentService).saveBestPracticeAssessment(singletonList(upsertBestPracticeAssessmentDTO), username);
    BestPracticeAssessmentDTO[] bestPracticeAssessmentDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeAssessmentDTO[].class);
    assertThat(bestPracticeAssessmentDTOs, arrayWithSize(1));
    assertThat(bestPracticeAssessmentDTOs, hasItemInArray(bestPracticeAssessmentDTO));
  }
}