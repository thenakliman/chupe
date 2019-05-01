package org.thenakliman.chupe.mappings;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class FundTransformerTest {

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

  private Fund getFund(int id, long amount) {
    Fund fund = new Fund();
    fund.setId(id);
    fund.setAmount(amount);
    fund.setApproved(false);
    return fund;
  }

  @Test
  public void shouldReturnTeamFundWhenOnlyOneTeamMemberRecordExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
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
        ownerUsername,
        TransactionType.CREDIT,
        false);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    expectedTeamFund.getTeamMemberFunds()
        .stream()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner() == actualTeamMemberFund.getOwner())
                    .findFirst().get())));
  }

  @Test
  public void shouldReturnTeamFundWhenOnlyOneTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(10, 143);
    addFundOwner(fund, ownerUsername, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        244,
        ownerUsername,
        TransactionType.CREDIT,
        false);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    expectedTeamFund.getTeamMemberFunds()
        .stream()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner() == actualTeamMemberFund.getOwner())
                    .findFirst().get())));
  }

  @Test
  public void shouldReturnTeamFundWhenMoreThanOneTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    int amount2 = 143;
    fund = getFund(11, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamMemberFund teamMemberFund = new TeamMemberFund(
        244,
        ownerUsername1,
        TransactionType.CREDIT,
        false);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(143, ownerUsername2, TransactionType.CREDIT, false);
    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner().equals(actualTeamMemberFund.getOwner()))
                    .findFirst().get())));
  }

  @Test
  public void shouldReturnTeamFundForDebitTeamMemberRecordsExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    int amount2 = 143;
    fund = getFund(11, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.CREDIT);
    addFundType(fund);
    funds.add(fund);

    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -42,
        ownerUsername1,
        TransactionType.CREDIT,
        false);
    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(143, ownerUsername2, TransactionType.CREDIT, false);
    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner().equals(actualTeamMemberFund.getOwner()))
                    .findFirst().get())));
  }

  @Test
  public void shouldReturnTeamFundForDebitTeamMemberRecordExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername = "fund-owner-username";
    addFundOwner(fund, ownerUsername, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    TeamFund expectedTeamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -101,
        ownerUsername,
        TransactionType.DEBIT,
        false);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);
    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner().equals(actualTeamMemberFund.getOwner()))
                    .findFirst().get())));
  }

  @Test
  public void shouldReturnTeamFundWhenMoreThanOneTeamMemberDebitRecordsExist() {
    List<Fund> funds = new ArrayList();
    int id = 10;
    int amount = 101;
    Fund fund = getFund(id, amount);
    String ownerUsername1 = "fund-owner-username";
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    int amount2 = 143;
    fund = getFund(11, amount2);
    addFundOwner(fund, ownerUsername1, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);

    fund = getFund(12, amount2);
    String ownerUsername2 = "fund-owner-username1";
    addFundOwner(fund, ownerUsername2, TransactionType.DEBIT);
    addFundType(fund);
    funds.add(fund);


    TeamMemberFund teamMemberFund = new TeamMemberFund(
        -244,
        ownerUsername1,
        TransactionType.DEBIT,
        false);

    List<TeamMemberFund> teamMemberFunds = new ArrayList<>();
    teamMemberFunds.add(teamMemberFund);
    teamMemberFund = new TeamMemberFund(
        -143,
        ownerUsername2,
        TransactionType.DEBIT,
        false);

    teamMemberFunds.add(teamMemberFund);
    TeamFund expectedTeamFund = new TeamFund();
    expectedTeamFund.setTeamMemberFunds(teamMemberFunds);

    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);

    expectedTeamFund.getTeamMemberFunds()
        .forEach(expectedTeamMemberFund -> assertThat(
            expectedTeamMemberFund,
            samePropertyValuesAs(
                teamFund.getTeamMemberFunds().stream().filter(actualTeamMemberFund ->
                    expectedTeamMemberFund.getOwner().equals(actualTeamMemberFund.getOwner()))
                    .findFirst().get())));
  }
}
