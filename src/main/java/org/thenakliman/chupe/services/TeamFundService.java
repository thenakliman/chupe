package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
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


@Service
public class TeamFundService {
  @Autowired
  private TeamFundRepository teamFundRepository;

  @Autowired
  private FundTypeRepository fundTypeRepository;

  @Autowired
  private FundTransformer fundTransformer;

  @Autowired
  private Converter converter;

  @Autowired
  private UserService userService;

  public List<FundType> getAllFundTypes() throws NotFoundException {
    List<FundType> fundTypes = fundTypeRepository.findAll();
    if (fundTypes.isEmpty()) {
      throw new NotFoundException("TeamMemberFund types could not found");
    }
    return fundTypes;
  }

  public TeamFund getTeamFund() {
    List<Fund> funds = teamFundRepository.findAll();
    TeamFund teamFund = fundTransformer.transformToTeamFund(funds);
    List<UserDTO> allUsers = userService.getAllUsers();
    Set<String> teamMembers = new HashSet<>();
    teamFund.getTeamMemberFunds().forEach(teamMember -> teamMembers.add(teamMember.getOwner()));
    allUsers.stream().filter(user -> !teamMembers.contains(user.getUserName())).forEach(user -> {
      teamFund.getTeamMemberFunds().add(
          new TeamMemberFund(
              0,
              user.getUserName(),
              TransactionType.DEBIT,
              false));
    });

    return teamFund;
  }

  public FundDTO saveTeamFund(FundDTO fundDTO) throws NotFoundException {
    Fund fund = converter.convertToObject(fundDTO, Fund.class);

    Optional<FundType> fundType = fundTypeRepository.findById(fundDTO.getType());

    if (!fundType.isPresent()) {
      String fundTypeNotFound = format("Fund type with id %d not found.", fundDTO.getId());
      throw new NotFoundException(fundTypeNotFound);
    }
    fund.setType(fundType.get());

    User addedBy = userService.findByUserName(fundDTO.getAddedBy());
    if (isNull(addedBy)) {
      String addedByUserNotFound = "Added by user with username %s not found";;
      throw new NotFoundException(format(addedByUserNotFound, fundDTO.getAddedBy()));
    }
    fund.setAddedBy(addedBy);

    User owner = userService.findByUserName(fundDTO.getOwner());
    if (isNull(owner)) {
      String ownerUserNotFound = "Owner user with username %s not found";
      throw new NotFoundException(format(ownerUserNotFound, fundDTO.getOwner()));
    }

    fund.setOwner(owner);

    return converter.convertToObject(teamFundRepository.save(fund), FundDTO.class);
  }

  public List<FundDTO> getFundForATeamMember(String username) throws NotFoundException {
    User user = new User();
    user.setUserName(username);
    List<Fund> teamMemberFunds = teamFundRepository.findByOwner(user);
    if (isNull(teamMemberFunds)) {
      throw new NotFoundException("Not Found");
    }

    return teamMemberFunds
        .stream()
        .map(teamMemberFund -> converter.convertToObject(teamMemberFund, FundDTO.class))
        .collect(toList());
  }
}
