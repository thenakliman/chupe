package org.thenakliman.chupe.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.FundTypeDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.services.TeamFundService;
import org.thenakliman.chupe.services.TokenService;


@WebMvcTest(controllers = TeamFundController.class)
public class TeamFundControllerTest extends BaseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private TeamFundService teamFundService;

  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;

  private ObjectMapper objectMapper;

  private Authentication authToken;

  private final String username = "username";

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
  public void shouldReturnAllFundTypes() throws Exception {
    FundTypeDTO fundType = new FundTypeDTO();
    fundType.setType("BIRTHDAY");
    fundType.setId(10);
    fundType.setDefaultAmount(100);

    List<FundTypeDTO> fundTypes = new ArrayList<>();
    fundTypes.add(fundType);

    when(teamFundService.getAllFundTypes()).thenReturn(fundTypes);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/team-funds/types")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();


    List<FundType> actualFundTypes = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new ArrayList<FundTypeDTO>().getClass());

    assertEquals(actualFundTypes.size(), 1);
    assertThat(actualFundTypes, samePropertyValuesAs(fundTypes));
  }

  @Test
  public void shouldReturnAllFundController() throws Exception {
    TeamFund teamFund = new TeamFund();
    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        10,
        "fund-owner");

    teamFund.setTeamMemberFunds(teamMemberFunds);

    when(teamFundService.getTeamFund()).thenReturn(teamFund);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/team-funds")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(teamFund))
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    TeamFund actualTeamFund = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        TeamFund.class);

    assertEquals(teamFund, actualTeamFund);
  }

  @Test
  public void shouldSaveTeamFundController() throws Exception {
    UpsertFundDTO fund = new UpsertFundDTO();
    fund.setTransactionType(TransactionType.CREDIT);
    fund.setAmount(1000);

    FundDTO fundDTO = new FundDTO();
    fundDTO.setTransactionType(TransactionType.CREDIT);
    fundDTO.setAmount(1000);
    when(teamFundService.saveTeamFund(fund, username)).thenReturn(fundDTO);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/team-funds")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(fund))
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    FundDTO actualFund = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        FundDTO.class);

    assertEquals(fundDTO, actualFund);
  }

  @Test
  public void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
    UpsertFundDTO fund = new UpsertFundDTO();
    fund.setTransactionType(TransactionType.CREDIT);
    fund.setAmount(1000);

    when(teamFundService.saveTeamFund(fund, "user2")).thenThrow(new NotFoundException("Not Found"));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/team-funds")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
  }

  @Test
  public void shouldReturnAllFundsDTOs() throws Exception {
    FundDTO fundDTO = new FundDTO();
    fundDTO.setId(10);
    fundDTO.setTransactionType(TransactionType.CREDIT);
    fundDTO.setApproved(false);
    fundDTO.setAmount(1000);
    String fakeUser = "fakeUser";
    fundDTO.setOwner(fakeUser);

    List<FundDTO> fundDTOs = new ArrayList<>();
    fundDTOs.add(fundDTO);
    when(teamFundService.getFundForATeamMember(anyString())).thenReturn(asList(fundDTO));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/funds?owner=" + fakeUser)
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    List<FundDTO> actualFundDTOs = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new ArrayList<FundDTO>().getClass());

    assertThat(actualFundDTOs, samePropertyValuesAs(fundDTOs));
  }
}