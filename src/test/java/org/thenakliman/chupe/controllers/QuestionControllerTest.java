package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
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

import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.services.QuestionService;


@WebMvcTest(QuestionController.class)
@RunWith(SpringRunner.class)
public class QuestionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private QuestionService questionService;

  @InjectMocks
  private QuestionController questionController;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  private ObjectMapper objectMapper;

  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
  }

  @Test
  public void shouldAddQuestions() throws  Exception {
    Question question = new Question();
    question.setQuestion("What is your name?");
    question.setAssignedTo("testUser1");
    question.setDescription("Need your name for auth service");
    question.setOwner("testUser2");
    question.setId(19);

    BDDMockito.given(questionService.addQuestion(any())).willReturn(question);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/question")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(question)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    Question result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), Question.class);

    assertThat(question, samePropertyValuesAs(result));
  }
}