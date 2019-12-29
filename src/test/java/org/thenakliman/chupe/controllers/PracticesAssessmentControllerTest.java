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
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.PracticesAssessmentService;
import org.thenakliman.chupe.services.TokenService;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PracticesAssessmentController.class, secure = false)
@RunWith(SpringRunner.class)
public class PracticesAssessmentControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private PracticesAssessmentService practicesAssessmentService;

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
  public void shouldSavePracticeAssessment() throws Exception {
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
    when(practicesAssessmentService.saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), username))
            .thenReturn(bestPracticeAssessmentDTO);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/retros/12345/practices-assessment")
            .content(objectMapper.writeValueAsBytes(singletonList(bestPracticeAssessmentAnswerDTO)))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

    verify(practicesAssessmentService).saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), username);
    BestPracticeAssessmentDTO bestPracticeAssessmentDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeAssessmentDTO.class);
    assertThat(bestPracticeAssessmentDTOs.getAnswers(), hasSize(1));
    assertThat(bestPracticeAssessmentDTOs.getAnswers(), hasItem(bestPracticeAssessmentAnswerDTO));
  }

  @Test
  public void shouldGetBestPracticeAssessment() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BestPracticeAssessmentAnswerDTO bestPracticeAssessmentAnswerDTO = BestPracticeAssessmentAnswerDTO.builder()
            .bestPracticeId(100L)
            .answer(false)
            .build();

    long retroId = 12345L;
    when(practicesAssessmentService.getPracticesAssessment(retroId, username))
            .thenReturn(singletonList(bestPracticeAssessmentAnswerDTO));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/retros/12345/practices-assessment")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    verify(practicesAssessmentService).getPracticesAssessment(retroId, username);
    BestPracticeAssessmentAnswerDTO[] bestPracticeAssessmentDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BestPracticeAssessmentAnswerDTO[].class);
    assertThat(bestPracticeAssessmentDTOs, arrayWithSize(1));
    assertThat(bestPracticeAssessmentDTOs, arrayContaining(bestPracticeAssessmentAnswerDTO));
  }
}