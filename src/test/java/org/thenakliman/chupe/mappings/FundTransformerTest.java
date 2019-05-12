package org.thenakliman.chupe.mappings;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.services.UserService;


@RunWith(MockitoJUnitRunner.class)
public class FundTransformerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private FundTransformer fundTransformer;

  private void addFundType(Fund fund) {
    FundType fundType = new FundType();
    fundType.setId(10);
    fundType.setType("BIRTHDAY");
    fund.setType(fundType);
  }

  private void addFundOwner(Fund fund, String ownerUsername, TransactionType transactionType) {
    User fundOwner = new User();
    fundOwner.setUserName(ownerUsername);
    fund.setOwner(fundOwner);
    fund.setTransactionType(transactionType);
  }

  private Fund getFund(Long id, long amount) {
    Fund fund = new Fund();
    fund.setId(id);
    fund.setAmount(amount);
    fund.setApproved(false);
    return fund;
  }

  @Test
  public void shouldReturnTeamFundWhenOnlyOneTeamMemberRecordExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;

    Fund fund = getFund(id, amount);

    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        amount,
        ownerUsername);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    assertThat(expectedTeamFund.getTeamMemberFunds(), hasSize(1));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund ->
            assertThat(teamFund.getTeamMemberFunds(), hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundWhenOnlyOneTeamMemberRecordExistAndOtherTeamMember() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;

    Fund fund = getFund(id, amount);

    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    when(userService.getAllUsers()).thenReturn(asList(
        UserDTO.builder().userName("user2").build(),
        UserDTO.builder().userName("user1").build()));
    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        amount,
        ownerUsername);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFunds.add(TeamMemberFund.builder().owner("user1").build());
    teamMemberFunds.add(TeamMemberFund.builder().owner("user2").build());
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    assertThat(teamFund.getTeamMemberFunds(), hasSize(3));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund ->
            assertThat(teamFund.getTeamMemberFunds(), hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundWhenOnlyOneTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(10L, 143);
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        244,
        ownerUsername);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    assertThat(teamFund.getTeamMemberFunds(), hasSize(1));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(teamFund.getTeamMemberFunds(),
            hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundWhenMoreThanOneTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    int amount2 = 143;
    fund = getFund(11L, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12L, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamMemberFund teamMemberFund = new TeamMemberFund(
        244,
        ownerUsername1);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(143, ownerUsername2);
    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    assertThat(teamFund.getTeamMemberFunds(), hasSize(2));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund ->
            assertThat(teamFund.getTeamMemberFunds(), hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundForDebitTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    Long amount2 = 143L;
    fund = getFund(11L, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12L, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -42,
        ownerUsername1);
    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(143, ownerUsername2);
    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    assertThat(teamFund.getTeamMemberFunds(), hasSize(2));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund ->
            assertThat(teamFund.getTeamMemberFunds(), hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundForDebitTeamMemberRecordExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -101,
        ownerUsername);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);
    assertThat(teamFund.getTeamMemberFunds(), hasSize(1));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund ->
            assertThat(teamFund.getTeamMemberFunds(), hasItem(expectedTeamMemberFund)));
  }

  @Test
  public void shouldReturnTeamFundWhenMoreThanOneTeamMemberDebitRecordsExist() {
    List<Fund> funds = new ArrayList();
    Long id = 10L;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    Long amount2 = 143L;
    fund = getFund(11L, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12L, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);


    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -244,
        ownerUsername1);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(
        -143,
        ownerUsername2);

    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    assertThat(teamFund.getTeamMemberFunds(), hasSize(2));
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(teamFund.getTeamMemberFunds(),
            hasItem(expectedTeamMemberFund)));
  }
}
