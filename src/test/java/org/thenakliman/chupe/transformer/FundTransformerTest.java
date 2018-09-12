package org.thenakliman.chupe.transformer;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.FundTypes;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class FundTransformerTest {

  @InjectMocks
  private FundTransformer fundTransformer;

  private void addFundType(Fund fund) {
    FundType fundType = new FundType();
    fundType.setId(10);
    fundType.setType(FundTypes.BIRTHDAY);
    fund.setType(fundType);
  }

  private String addFundOwner(Fund fund, String ownerUsername, TransactionType transactionType) {
    User fundOwner = new User();
    fundOwner.setUserName(ownerUsername);
    fund.setOwner(fundOwner);
    fund.setTransactionType(transactionType);
    return ownerUsername;
  }

  private Fund getFund(int id, long amount) {
    Fund fund = new Fund();
    fund.setId(id);
    fund.setAmount(amount);
    fund.setApproved(false);
    return fund;
  }

  private FundDTO getFundDTO(Date createdAt) {
    return new FundDTO(
        10,
        11,
        1000,
        "test-owner",
        "test-added-by-user",
        TransactionType.CREDIT,
        false,
        createdAt);
  }

  private Fund getFundWithDetailedData(Date createdAt) {
    Fund fund = new Fund();
    fund.setId(10);
    fund.setAmount(1000);
    fund.setTransactionType(TransactionType.CREDIT);
    fund.setApproved(false);
    fund.setCreatedAt(createdAt);

    FundType fundType = new FundType();
    fundType.setId(11);
    fund.setType(fundType);

    User owner = new User();
    owner.setUserName("test-owner");
    fund.setOwner(owner);

    User addedBy = new User();
    addedBy.setUserName("test-added-by-user");
    fund.setAddedBy(addedBy);
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

  @Test
  public void shouldTransformFunddDtoToFundModel() {
    Fund fund = new Fund();
    fund.setId(10);
    fund.setAmount(1000);
    fund.setTransactionType(TransactionType.CREDIT);
    fund.setApproved(false);
    Date createdAt = new Date();
    fund.setCreatedAt(createdAt);
    String owner = "test-owner";
    String addedBy = "test-added-by-user";
    FundDTO fundDTO = new FundDTO(
        10,
        11,
        1000,
        owner,
        addedBy,
        TransactionType.CREDIT,
        false,
        createdAt);;

    Fund actualFund = fundTransformer.transformToFund(fundDTO);

    assertEquals(fund.getAddedBy(), actualFund.getAddedBy());
    assertEquals(fund.getOwner(), actualFund.getOwner());
    assertEquals(fund.getAmount(), actualFund.getAmount());
    assertEquals(fund.getId(), actualFund.getId());
    assertEquals(fund.getTransactionType(), actualFund.getTransactionType());
    assertEquals(fund.getType(), actualFund.getType());
    assertEquals(fund.getCreatedAt(), actualFund.getCreatedAt());
  }


  @Test
  public void shouldConvertNegativeToPositiveFundAmountWhenTransformFunddtoToFundModel() {
    Fund fund = new Fund();
    fund.setId(10);
    fund.setAmount(1000);
    fund.setTransactionType(TransactionType.CREDIT);
    fund.setApproved(false);
    Date createdAt = new Date();
    fund.setCreatedAt(createdAt);
    String owner = "test-owner";
    String addedBy = "test-added-by-user";
    FundDTO fundDTO = new FundDTO(
        10,
        11,
        -1000,
        owner,
        addedBy,
        TransactionType.CREDIT,
        false,
        createdAt);;

    Fund actualFund = fundTransformer.transformToFund(fundDTO);

    assertEquals(fund.getAddedBy(), actualFund.getAddedBy());
    assertEquals(fund.getOwner(), actualFund.getOwner());
    assertEquals(fund.getAmount(), actualFund.getAmount());
    assertEquals(fund.getId(), actualFund.getId());
    assertEquals(fund.getTransactionType(), actualFund.getTransactionType());
    assertEquals(fund.getType(), actualFund.getType());
    assertEquals(fund.getCreatedAt(), actualFund.getCreatedAt());
  }

  @Test
  public void shouldTransformFundModeltoFundDTO() {
    Date createdAt = new Date();
    FundDTO fundDTO = getFundDTO(createdAt);
    Fund fund = getFundWithDetailedData(createdAt);
    FundDTO actualFund = fundTransformer.transformToFundDTO(fund);
    assertThat(fundDTO, samePropertyValuesAs(actualFund));
  }

  @Test
  public void shouldTransformFundModelToFundDTOs() {
    Date createdAt = new Date();
    FundDTO fundDTO = getFundDTO(createdAt);
    List<Fund> funds = Arrays.asList(getFundWithDetailedData(createdAt));
    List<FundDTO> actualFund = fundTransformer.transformToFundDTOs(funds);
    assertEquals(actualFund.size(), funds.size());
    assertThat(fundDTO, samePropertyValuesAs(actualFund.get(0)));
  }
}
