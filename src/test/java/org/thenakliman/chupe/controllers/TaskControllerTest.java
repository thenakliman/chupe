package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.TaskService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest extends BaseControllerTest {
  private final String username = "username";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @MockBean
  private TaskService taskService;
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

  /**
   * Setup web application context.
   */
  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    /* NOTE(thenakliman) this approach has been used to bypass authentication
     * layer from the test. It creates a standalone application with single
     * controller(TaskController).
     * */
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);
  }

  @Test
  public void shouldTaskForAUser() throws Exception {
    String description = "today's only task";
    TaskDTO taskDTO = TaskDTO.builder().description(description).build();
    List<TaskDTO> taskDTOs = new ArrayList<>();
    taskDTOs.add(taskDTO);
    given(taskService.getAllTask(username)).willReturn(taskDTOs);

    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/tasks")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<TaskDTO> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), new ArrayList<TaskDTO>().getClass());

    assertEquals(result.size(), 1);
    assertThat(result, samePropertyValuesAs(taskDTOs));
  }

  @Test
  public void shouldGiveNotFoundWhenTaskIsNotFound() throws Exception {
    given(taskService.getAllTask(username)).willThrow(new NotFoundException("Not Found"));

    SecurityContextHolder.getContext().setAuthentication(authToken);
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/tasks")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void shouldCreateTask() throws Exception {
    String description = "today's only task";
    TaskDTO taskDTO = TaskDTO.builder().description(description).build();
    SecurityContextHolder.getContext().setAuthentication(authToken);
    given(taskService.saveTask(taskDTO, username)).willReturn(taskDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(taskDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    TaskDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), TaskDTO.class);

    assertThat(result, samePropertyValuesAs(taskDTO));
  }

  @Test
  public void shouldUpdateTask() throws Exception {
    String description = "today's only task";
    TaskDTO taskDTO = TaskDTO.builder().description(description).build();
    long taskId = 10L;
    given(taskService.updateTask(taskId, taskDTO)).willReturn(taskDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/tasks/" + taskId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(taskDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    TaskDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), TaskDTO.class);

    assertThat(result, samePropertyValuesAs(taskDTO));
  }

  @Test
  public void shouldRaiseNotFoundWhenUpdateTask() throws Exception {
    String description = "today's only task";
    TaskDTO taskDTO = TaskDTO.builder().description(description).build();
    long taskId = 10L;
    given(taskService.updateTask(taskId, taskDTO)).willThrow(new NotFoundException("not Found"));

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/tasks/" + taskId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(taskDTO)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}