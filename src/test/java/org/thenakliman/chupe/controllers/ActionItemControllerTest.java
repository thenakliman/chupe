package org.thenakliman.chupe.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;

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
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.ActionItemService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = ActionItemController.class)
@RunWith(SpringRunner.class)
public class ActionItemControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private ActionItemService actionItemService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private String username = "user-name";

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
  public void shouldGetActionItems() throws Exception {
    ArrayList<ActionItem> actionItems = new ArrayList<>();
    actionItems.add(new ActionItem());

    BDDMockito.given(actionItemService.getActionItems(eq(username))).willReturn(actionItems);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/action-items")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();


    List<ActionItem> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), List.class);

    assertThat(result, hasSize(1));
  }
}