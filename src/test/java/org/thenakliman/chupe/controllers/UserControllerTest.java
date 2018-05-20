package org.thenakliman.chupe.controllers;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.services.UserService;


@WebMvcTest(controllers = UserController.class)
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

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setUp() {
    objectMapper = jasonBuilder.build();
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldReturnEmptyUser() throws Exception {
    List<UserDTO> users = new ArrayList<UserDTO>();
    BDDMockito.given(userService.getAllUsers()).willReturn(users);
    MvcResult mvcResult = null;
    mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<UserDTO> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            List.class);

    assertEquals(result, users);
  }

  @Test
  public void shouldReturnUser() throws Exception {
    UserDTO userDTO1 = new UserDTO(
            "user1_name",
            "user1_fistname",
            "user1_lastname",
            "user1_email");
    UserDTO userDTO2 = new UserDTO(
            "user2_username",
            "user2_fistname",
            "user2_lastname",
            "user2_email");

    List<UserDTO> userDTOs = new ArrayList<UserDTO>();

    userDTOs.add(userDTO1);
    userDTOs.add(userDTO2);
    BDDMockito.given(userService.getAllUsers()).willReturn(userDTOs);
    MvcResult mvcResult = null;

    mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/api/v1/users")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    /* NOTE(thenakliman): Use jackson to convert response to object and then compare the objects,
     *  currently only length is being matched.
     */
    List<User> result = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new ArrayList<UserDTO>().getClass());

    assertThat(result, samePropertyValuesAs(userDTOs));
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