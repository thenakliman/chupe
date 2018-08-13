package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.FundTypes.BIRTHDAY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;
import org.thenakliman.chupe.transformer.FundTransformer;

@RunWith(MockitoJUnitRunner.class)
public class TeamFundServiceTest {
  @Mock
  private FundTypeRepository fundTypeRepository;

  @Mock
  private TeamFundRepository teamFundRepository;

  @Mock
  private FundTransformer fundTransformer;

  @InjectMocks
  private TeamFundService teamFundService;

  private List<FundType> getFundTypes() {
    FundType fundType = new FundType();
    fundType.setId(1);
    fundType.setDescription("description");
    fundType.setType(BIRTHDAY);
    List fundTypes = new ArrayList();
    fundTypes.add(fundType);
    return fundTypes;
  }

  private List<Fund> getFunds() {
    Fund fund = new Fund();
    fund.setId(1);
    fund.setAmount(10);
    List<Fund> funds = new ArrayList();
    funds.add(fund);
    return funds;
  }

  private TeamFund getTeamFund() {
    TeamFund teamFund = new TeamFund();
    TeamMemberFund teamMemberFund = new TeamMemberFund(10, null, null, false);
    List teamMemberFunds = new ArrayList();
    teamMemberFunds.add(teamMemberFund);
    teamFund.setTeamMemberFunds(teamMemberFunds);
    return teamFund;
  }

  @Test
  public void shouldReturnAllTeamFundType() throws NotFoundException {
    when(fundTypeRepository.findAll()).thenReturn(getFundTypes());
    List<FundType> fundTypes = teamFundService.getAllFundTypes();
    assertThat(getFundTypes(), samePropertyValuesAs(fundTypes));
  }

  @Test
  public void shouldReturnTeamFund() throws NotFoundException {
    List<Fund> funds = getFunds();
    when(teamFundRepository.findAll()).thenReturn(funds);
    when(fundTransformer.transformToTeamFund(funds)).thenReturn(getTeamFund());
    TeamFund teamFund = teamFundService.getTeamFund();
    assertThat(getTeamFund().getTeamMemberFunds(),
               samePropertyValuesAs(teamFund.getTeamMemberFunds()));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundException() throws NotFoundException {
    when(teamFundRepository.findAll()).thenReturn(Collections.emptyList());
    teamFundService.getTeamFund();
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaiseExceptionWhenFundTypeNotFound() throws NotFoundException {
    when(fundTypeRepository.findAll()).thenReturn(Collections.emptyList());
    teamFundService.getAllFundTypes();
  }
}