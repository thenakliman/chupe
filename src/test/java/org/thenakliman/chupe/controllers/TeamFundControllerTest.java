package org.thenakliman.chupe.controllers;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.FundTypes;
import org.thenakliman.chupe.services.TeamFundService;



@WebMvcTest(controllers = TeamFundControllerTest.class)
public class TeamFundControllerTest extends BaseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private TeamFundService teamFundService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  private ObjectMapper objectMapper;

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
  }

  @Test
  public void shouldReturnAllFundTypes() throws Exception {
    FundType fundType = new FundType();
    fundType.setType(FundTypes.BIRTHDAY);
    fundType.setId(10);
    fundType.setDefaultAmount(100);

    List<FundType> fundTypes = new ArrayList<>();
    fundTypes.add(fundType);

    when(teamFundService.getAllFundTypes()).thenReturn(fundTypes);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/team-funds/types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(fundType))
    ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
  }

  @Test
  public void shouldReturnEmptyFundTypes() throws Exception {
    when(teamFundService.getAllFundTypes()).thenThrow(new NotFoundException("No funds found"));
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/team-funds/types")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }
}