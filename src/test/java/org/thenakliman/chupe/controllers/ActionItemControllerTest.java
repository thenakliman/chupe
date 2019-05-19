package org.thenakliman.chupe.controllers;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.dto.ActionItemQueryParams;
import org.thenakliman.chupe.dto.UpdateActionItemDTO;
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
    ArrayList<ActionItemDTO> actionItemDtos = new ArrayList<>();
    actionItemDtos.add(ActionItemDTO.builder().build());
    BDDMockito.given(actionItemService.getActionItems(any(ActionItemQueryParams.class))).willReturn(actionItemDtos);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/retro-action-items")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<ActionItemDTO> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), List.class);

    assertThat(actionItemDtos, samePropertyValuesAs(result));
  }

  @Test
  public void shouldCreateActionItem() throws Exception {
    ActionItemDTO actionItemDTO = ActionItemDTO.builder().id(102L).build();
    BDDMockito.given(actionItemService.addActionItem(any(), anyString())).willReturn(actionItemDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/retro-action-items")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(actionItemDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    ActionItemDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), ActionItemDTO.class);

    assertThat(actionItemDTO, samePropertyValuesAs(result));
  }

  @Test
  public void shouldReturnUpdatedActionItem() throws Exception {
    Long actionItemDto = 1000L;
    ActionItemDTO actionItemDTO = ActionItemDTO.builder().build();
    UpdateActionItemDTO updateActionItemDto = new UpdateActionItemDTO();
    updateActionItemDto.setDescription("description");

    SecurityContextHolder.getContext().setAuthentication(authToken);

    BDDMockito.given(
        actionItemService.updateActionItem(actionItemDto, updateActionItemDto, username))
        .willReturn(actionItemDTO);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/retro-action-items/" + actionItemDto)
        .content(objectMapper.writeValueAsString(updateActionItemDto))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    ActionItemDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), ActionItemDTO.class);

    assertThat(actionItemDTO, samePropertyValuesAs(result));

  }
}