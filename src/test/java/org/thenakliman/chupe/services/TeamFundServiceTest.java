package org.thenakliman.chupe.services;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.FundTypeDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.dto.UpsertFundDTO;
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
  private Converter converter;

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

  private FundTypeDTO getFundTypeDTO() {
    FundTypeDTO fundType = new FundTypeDTO();
    fundType.setId(1);
    fundType.setDescription("description");
    fundType.setType("BIRTHDAY");
    return fundType;
  }

  private Fund getFund() {
    Fund fund = new Fund();
    fund.setId(1L);
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

  private UpsertFundDTO getUpsertFundDTO() {
    UpsertFundDTO fundDTO = new UpsertFundDTO();
    fundDTO.setAmount(1000);
    fundDTO.setOwner("Lucky");
    fundDTO.setTransactionType(TransactionType.CREDIT);
    fundDTO.setType(1111);
    return fundDTO;
  }

  private TeamFund getTeamFund() {
    List teamMemberFunds = new ArrayList();
    teamMemberFunds.add(new TeamMemberFund(0, "user1"));
    teamMemberFunds.add(new TeamMemberFund(0, "user2"));
    teamMemberFunds.add(new TeamMemberFund(10, null));
    TeamFund teamFund = new TeamFund();
    teamFund.setTeamMemberFunds(teamMemberFunds);
    return teamFund;
  }

  @Test
  public void shouldReturnAllTeamFundType() {
    when(fundTypeRepository.findAll()).thenReturn(getFundTypes());
    List<FundTypeDTO> mockedListOfFundDtos = mock(List.class);
    when(converter.convertToListOfObjects(anyList(), eq(FundTypeDTO.class))).thenReturn(mockedListOfFundDtos);
    List<FundTypeDTO> fundTypes = teamFundService.getAllFundTypes();
    assertThat(fundTypes, is(mockedListOfFundDtos));
  }

  @Test
  public void shouldReturnAllFundForAUser() {
    User user = new User();
    String username = "test-username";
    user.setUserName(username);
    when(teamFundRepository.findByOwnerUserName(anyString())).thenReturn(emptyList());

    List<FundDTO> fundsForAUser = teamFundService.getFundForATeamMember(username);

    assertThat(fundsForAUser, hasSize(0));
  }

  @Test
  public void shouldReturnTeamFund() throws NotFoundException {
    List<Fund> funds = getFunds();
    when(teamFundRepository.findAll()).thenReturn(funds);

    List<UserDTO> users = new ArrayList<>();
    users.add(getUserDTO("user1"));
    users.add(getUserDTO("user2"));
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

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenInvalidType() throws NotFoundException {
    Fund fund = getFund();
    UpsertFundDTO fundDTO = getUpsertFundDTO();
    given(converter.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.empty());

    teamFundService.saveTeamFund(fundDTO, "user22");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldReturnNotFoundExceptionInvalidOwner() throws NotFoundException {
    Fund fund = getFund();
    UpsertFundDTO fundDTO = getUpsertFundDTO();
    given(userService.findByUserName(fundDTO.getOwner())).willThrow(new UsernameNotFoundException(""));
    given(converter.convertToObject(fundDTO, Fund.class)).willReturn(fund);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));

    teamFundService.saveTeamFund(fundDTO, "user1");
  }

  @Test
  public void shouldReturnUserDTOOnSave() throws NotFoundException {
    Fund fund = getFund();
    UpsertFundDTO upsertFundDTO = getUpsertFundDTO();
    FundDTO FundDTO = getFundDTO();
    given(converter.convertToObject(upsertFundDTO, Fund.class)).willReturn(fund);
    given(teamFundRepository.save(fund)).willReturn(fund);
    given(converter.convertToObject(fund, FundDTO.class)).willReturn(FundDTO);
    given(fundTypeRepository.findById(1111L)).willReturn(Optional.of(getFundType()));

    FundDTO actualFundDTO = teamFundService.saveTeamFund(upsertFundDTO, "user4");

    assertThat(FundDTO, samePropertyValuesAs(actualFundDTO));
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