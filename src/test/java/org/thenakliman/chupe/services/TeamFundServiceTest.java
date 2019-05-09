package org.thenakliman.chupe.services;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.mappings.FundTransformer;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;


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

  @Mock
  private ConverterUtil converterUtil;

  @InjectMocks
  private TeamFundService teamFundService;

  private List<FundType> getFundTypes() {
    FundType fundType = new FundType();
    fundType.setId(1);
    fundType.setDescription("description");
    fundType.setType("BIRTHDAY");
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
    List teamMemberFunds = new ArrayList();
    teamMemberFunds.add(new TeamMemberFund(0, "user1", TransactionType.DEBIT, false));
    teamMemberFunds.add(new TeamMemberFund(0, "user2", TransactionType.DEBIT, false));
    teamMemberFunds.add(new TeamMemberFund(10, null, null, false));
    TeamFund teamFund = new TeamFund();
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
  public void shouldReturnAllFundForAUser() throws NotFoundException {
    User user = new User();
    String username = "test-username";
    user.setUserName(username);
    when(teamFundRepository.findByOwner(any())).thenReturn(emptyList());

    List<FundDTO> fundsForAUser = teamFundService.getFundForATeamMember(username);

    assertThat(fundsForAUser, hasSize(0));
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenUserIsNotFound() throws NotFoundException {
    User user = new User();
    String username = "test-username";
    user.setUserName(username);
    when(teamFundRepository.findByOwner(any())).thenReturn(null);
    teamFundService.getFundForATeamMember(username);
  }

  @Test
  public void shouldReturnTeamFund() throws NotFoundException {
    List<Fund> funds = getFunds();
    when(teamFundRepository.findAll()).thenReturn(funds);

    List<UserDTO> users = new ArrayList<>();
    users.add(getUserDTO("user1"));
    users.add(getUserDTO("user2"));
    when(userService.getAllUsers()).thenReturn(users);
    when(fundTransformer.transformToTeamFund(funds)).thenReturn(getTeamFund());
    List<TeamMemberFund> teamMemberFunds = teamFundService.getTeamFund().getTeamMemberFunds();
    List<TeamMemberFund> expectedTeamMemberFunds = getTeamFund().getTeamMemberFunds();
    assertThat(expectedTeamMemberFunds.get(0),
        samePropertyValuesAs(teamMemberFunds.get(0)));
    assertThat(expectedTeamMemberFunds.get(1),
        samePropertyValuesAs(teamMemberFunds.get(1)));
    assertThat(expectedTeamMemberFunds.get(2),
        samePropertyValuesAs(teamMemberFunds.get(2)));
  }

  @Test
  public void shouldReturnAllUsersWithZeroAmountWhenNotFundIsPresent() throws NotFoundException {
    List<UserDTO> users = new ArrayList<>();
    String username = "user1";
    users.add(getUserDTO(username));
    when(userService.getAllUsers()).thenReturn(users);
    when(teamFundRepository.findAll()).thenReturn(null);
    TeamFund teamFund1 = new TeamFund();
    teamFund1.setTeamMemberFunds(new ArrayList<>());
    when(fundTransformer.transformToTeamFund(any())).thenReturn(teamFund1);

    TeamFund teamFund = teamFundService.getTeamFund();

    TeamMemberFund teamMemberFund = new TeamMemberFund(0, username, TransactionType.DEBIT, false);
    assertThat(teamMemberFund, samePropertyValuesAs(teamFund.getTeamMemberFunds().get(0)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaiseExceptionWhenFundTypeNotFound() throws NotFoundException {
    when(fundTypeRepository.findAll()).thenReturn(emptyList());
    teamFundService.getAllFundTypes();
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenInvalidType() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(converterUtil.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.empty());

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionInvalidOwner() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(converterUtil.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));
    given(userService.findByUserName("James")).willReturn(null);

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionInvalidAddedBy() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(converterUtil.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));
    given(userService.findByUserName("James")).willReturn(getUser("James"));
    given(userService.findByUserName("Lucky")).willReturn(null);

    teamFundService.saveTeamFund(fundDTO);
  }

  @Test
  public void shouldReturnUserDTOOnSave() throws NotFoundException {
    Fund fund = getFund();
    FundDTO fundDTO = getFundDTO();
    given(converterUtil.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(teamFundRepository.save(fund)).willReturn(fund);
    given(converterUtil.convertToObject(fund, FundDTO.class)).willReturn(fundDTO);
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

  private UserDTO getUserDTO(String username) {
    UserDTO user = new UserDTO();
    user.setUserName(username);
    return user;
  }

  private FundType getFundType() {
    FundType fundType = new FundType();
    fundType.setType("BIRTHDAY");
    return fundType;
  }
}