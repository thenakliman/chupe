package org.thenakliman.chupe.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.BestPracticeAssessmentService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = RetroBestPracticeAssessmentController.class, secure = false)
@RunWith(SpringRunner.class)
public class RetroBestPracticeAssessmentControllerTest {
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
  public void shouldGetBestPracticeAssessment() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BestPracticeAssessmentAnswerDTO bestPracticeAssessmentAnswerDTO = BestPracticeAssessmentAnswerDTO.builder()
        .bestPracticeId(100L)
        .answer(false)
        .build();

    BestPracticeAssessmentDTO bestPracticeAssessmentDTO = BestPracticeAssessmentDTO.builder()
        .answers(singletonList(bestPracticeAssessmentAnswerDTO))
        .id(1222L)
        .build();

    long retroId = 12345L;
    when(bestPracticeAssessmentService.saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), username))
        .thenReturn(bestPracticeAssessmentDTO);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retros/12345/best-practices-assessments")
        .content(objectMapper.writeValueAsBytes(singletonList(bestPracticeAssessmentAnswerDTO)))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    verify(bestPracticeAssessmentService).saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), username);
    BestPracticeAssessmentDTO bestPracticeAssessmentDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeAssessmentDTO.class);
    assertThat(bestPracticeAssessmentDTOs.getAnswers(), hasSize(1));
    assertThat(bestPracticeAssessmentDTOs.getAnswers(), hasItem(bestPracticeAssessmentAnswerDTO));
  }
}