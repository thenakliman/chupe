package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.services.QuestionService;


@WebMvcTest(controllers = QuestionController.class)
@RunWith(SpringRunner.class)
public class QuestionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private QuestionService questionService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  private ObjectMapper objectMapper;

  /** Setup web application context. */
  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    /* NOTE(thenakliman) this approach has been used to bypass authentication
    * layer from the test. It creates a standalone application with single
    * controller(QuestionController).
    * */
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldAddQuestions() throws  Exception {
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setQuestion("What is your name?");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setOwner("testUser2");
    questionDTO.setId(19);

    BDDMockito.given(questionService.addQuestion(any())).willReturn(questionDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/question")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    QuestionDTO result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), QuestionDTO.class);

    assertThat(questionDTO, samePropertyValuesAs(result));
  }

  @Test
  public void shouldReturnEmptyQuestion() throws Exception {
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    BDDMockito.given(questionService.getQuestions()).willReturn(questionDTOs);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/question")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<QuestionDTO> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            List.class);

    assertEquals(result, questionDTOs);
  }

  @Test
  public void shouldReturnInternalServerErrorIfThereIsAnException() throws Exception {
    /** NOTE(thenakliman): Fix willAnswer to willThrow, it is throwing errors
     * BDDMockito.given(userService.getQuestions()).willThrow(Exception.class); */

    BDDMockito.given(questionService.getQuestions()).willAnswer(invocation -> {
      throw new Exception(); });

    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void shouldReturnQuestions() throws Exception {
    QuestionDTO questionDTO1 = new QuestionDTO(10, "why?", "desc1", "user1", "user2");
    QuestionDTO questionDTO2 = new QuestionDTO(11, "when?", "desc2", "user4", "user3");

    List<QuestionDTO> questionDTOs = new ArrayList<>();

    questionDTOs.add(questionDTO1);
    questionDTOs.add(questionDTO2);
    BDDMockito.given(questionService.getQuestions()).willReturn(questionDTOs);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/question")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    /* NOTE(thenakliman): Use jackson to convert response to object and then compare the objects,
     *  currently only length is being matched.
     */
    List<QuestionDTO> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new ArrayList<UserDTO>().getClass());

    assertThat(result, samePropertyValuesAs(questionDTOs));
  }

  @Test
  public void shouldUpdateQuestions() throws Exception {
    long id = 10;
    QuestionDTO questionDTO = new QuestionDTO(id, "why?", "desc1", "user1", "user2");
    mockMvc.perform(MockMvcRequestBuilders
            .put("/api/v1/question/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(questionDTO))
      ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
  }

  @Test
  public void shouldReturnNotFoundStatusIfQuestionDoesNotExist() throws Exception {
    long id = 10;
    QuestionDTO questionDTO = new QuestionDTO(id, "why?", "desc1", "user1", "user2");
    doThrow(new NotFoundException("Question not found"))
        .when(questionService).updateQuestions(anyLong(), any());

    mockMvc.perform(MockMvcRequestBuilders
            .put("/api/v1/question/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(questionDTO))
      ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }
}