package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.FundTypes.BIRTHDAY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;
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

  @Mock
  private UserService userService;

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

  private Fund getFund() {
    Fund fund = new Fund();
    fund.setId(1);
    fund.setAmount(10);
    return fund;
  }

  private List<Fund> getFunds() {
    Fund fund = getFund();
    List<Fund> funds = new ArrayList();
    funds.add(fund);
    return funds;
  }

  private FundDTO getFundDTO() {
    FundDTO fundDTO = new FundDTO();
    fundDTO.setAddedBy("James");
    fundDTO.setAmount(1000);
    fundDTO.setApproved(false);
    fundDTO.setId(101);
    fundDTO.setOwner("Lucky");
    fundDTO.setTransactionType(TransactionType.CREDIT);
    fundDTO.setType(1111);
    return fundDTO;
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

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenInvalidType() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(fundTransformer.transformToFund(fundDTO)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.empty());

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionInvalidOwner() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(fundTransformer.transformToFund(fundDTO)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));
    given(userService.findByUserName("James")).willReturn(null);

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionInvalidAddedBy() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(fundTransformer.transformToFund(fundDTO)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));
    given(userService.findByUserName("James")).willReturn(getUser("James"));
    given(userService.findByUserName("Lucky")).willReturn(null);

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test
  public void shouldReturnUserDTOOnSave() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(fundTransformer.transformToFund(fundDTO)).willReturn(fund);
    given(fundTransformer.transformToFundDTO(any())).willReturn(fundDTO);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));
    given(userService.findByUserName("James")).willReturn(getUser("James"));
    given(userService.findByUserName("Lucky")).willReturn(getUser("Lucky"));

    FundDTO actualFundDTO = teamFundService.saveTeamFund(fundDTO);

    assertThat(fundDTO, samePropertyValuesAs(actualFundDTO));
  }

  private User getUser(String username) {
    User user = new User();
    user.setUserName(username);
    return user;
  }

  private FundType getFundType() {
    FundType fundType = new FundType();
    fundType.setType(BIRTHDAY);
    return fundType;
  }
}