package org.thenakliman.chupe.controllers;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.services.UserService;


@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
public class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private Jackson2ObjectMapperBuilder jasonBuilder;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = jasonBuilder.build();
  }

  @Test
  public void shouldReturnEmptyUser() throws Exception {
    List<User> expUsers = new ArrayList<User>();
    List<User> modelUsers = new ArrayList<User>();
    BDDMockito.given(userService.getAllUsers()).willReturn(modelUsers);
    MvcResult mvcResult = null;
    mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<User> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            List.class);

    assertEquals(result, expUsers);
  }

  @Test
  public void shouldReturnUser() throws Exception {
    User modelUser1 = new User("user1_fistname", "user1_lastname", "user1_username",
                         "user1_email", "user1_password", true);
    User modelUser2 = new User("user2_fistname", "user2_lastname", "user2_username",
                         "user2_email", "user2_password", false);
    List<User> modelUsers = new ArrayList<User>();
    modelUsers.add(modelUser1);
    modelUsers.add(modelUser2);
    BDDMockito.given(userService.getAllUsers()).willReturn(modelUsers);
    MvcResult mvcResult = null;

    mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    /* NOTE(thenakliman): Use jackson to convert response to object and then compare the objects,
     *  currently only length is being matched.
     */
    List<User> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new ArrayList<User>().getClass());

    assertEquals(result.size(), modelUsers.size());
  }

  @Test
  public void shouldReturnInternalServerErrorIfThereIsAnException() throws Exception {
    /** NOTE(thenakliman): Fix willAnswer to willThrow, it is throwing errors
     * BDDMockito.given(userService.getAllUsers()).willThrow(Exception.class); */

    BDDMockito.given(userService.getAllUsers()).willAnswer(invocation -> {
      throw new Exception(); });

    mockMvc.perform(MockMvcRequestBuilders
      .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}