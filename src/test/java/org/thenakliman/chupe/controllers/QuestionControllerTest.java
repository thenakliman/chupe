package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.QuestionPriority;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.services.QuestionService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = QuestionController.class, secure = false)
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
  public void shouldAddQuestions() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion("What is your name?");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    QuestionDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), QuestionDTO.class);

    assertThat(new QuestionDTO(), samePropertyValuesAs(result));
  }

  @Test
  public void shouldGiveBadRequestWhenAddQuestionsQuestionLengthIs8() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion("What is ");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGiveBadRequestWhenAddQuestionsQuestionLengthIs101() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion(getStringWithLength(101));
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("Need your name for auth service");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGiveBadRequestWhenAddQuestionsDescriptionLengthIs9() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion("hello question");
    questionDTO.setAssignedTo("testUser1");
    questionDTO.setDescription("123456789");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGiveBadRequestWhenAddQuestionsAssignedToLengthIs0() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion("hello question");
    questionDTO.setAssignedTo("");
    questionDTO.setDescription("123456789fdffdfdfd");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldGiveBadRequestWhenAddQuestionsAssignedToLengthIs257() throws Exception {
    UpsertQuestionDTO questionDTO = new UpsertQuestionDTO();
    questionDTO.setQuestion("hello question");
    questionDTO.setAssignedTo(getStringWithLength(257));
    questionDTO.setDescription("123456789fdfd");
    questionDTO.setPriority(QuestionPriority.LOW);
    questionDTO.setStatus(QuestionStatus.OPEN);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.addQuestion(any(), anyString())).willReturn(new QuestionDTO());

    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/questions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  private String getStringWithLength(int length) {
    return IntStream
        .range(0, length)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(""));
  }

  @Test
  public void shouldReturnEmptyQuestion() throws Exception {
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    BDDMockito.given(questionService.getQuestions()).willReturn(questionDTOs);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/questions")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<QuestionDTO> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        List.class);

    assertEquals(result, questionDTOs);
  }

  @Test
  public void shouldReturnInternalServerErrorIfThereIsAnException() throws Exception {
    /* NOTE(thenakliman): Fix willAnswer to willThrow, it is throwing errors
     * BDDMockito.given(userService.getQuestions()).willThrow(Exception.class); */
    SecurityContextHolder.getContext().setAuthentication(authToken);
    BDDMockito.given(questionService.getQuestions()).willAnswer(invocation -> {
      throw new NotFoundException("");
    });

    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/questions")).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void shouldReturnQuestions() throws Exception {
    QuestionDTO questionDTO1 = new QuestionDTO(
        10, "why?",
        "desc1", "user1",
        "user2", QuestionStatus.OPEN,
        QuestionPriority.LOW);

    QuestionDTO questionDTO2 = new QuestionDTO(
        11, "when?",
        "desc2", "user4",
        "user3", QuestionStatus.OPEN,
        QuestionPriority.LOW);

    List<QuestionDTO> questionDTOs = new ArrayList<>();

    questionDTOs.add(questionDTO1);
    questionDTOs.add(questionDTO2);
    BDDMockito.given(questionService.getQuestions()).willReturn(questionDTOs);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/questions")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
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
    QuestionDTO questionDTO = new QuestionDTO(
        id, "why? sure lets update",
        "description - 1", "user1",
        "user2", QuestionStatus.OPEN,
        QuestionPriority.LOW);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/questions/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO))
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void shouldReturnNotFoundStatusIfQuestionDoesNotExist() throws Exception {
    long id = 10;
    QuestionDTO questionDTO = new QuestionDTO(
        id, "why? fdsfsdafsdf",
        "descfsdfsadf1", "user1",
        "user2", QuestionStatus.OPEN,
        QuestionPriority.LOW);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    doThrow(new NotFoundException("Question not found"))
        .when(questionService).updateQuestions(anyLong(), any(), anyString());

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/questions/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(questionDTO))
    ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }
}