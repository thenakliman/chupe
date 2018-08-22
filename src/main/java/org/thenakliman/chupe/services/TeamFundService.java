package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;
import org.thenakliman.chupe.transformer.FundTransformer;

import java.util.List;
import java.util.Optional;

@Service
public class TeamFundService {
  @Autowired
  private TeamFundRepository teamFundRepository;

  @Autowired
  private FundTypeRepository fundTypeRepository;

  @Autowired
  private FundTransformer fundTransformer;

  @Autowired
  private UserService userService;

  /** Get all fund types.
   *
   * @returns list of fund types.
   */
  public List<FundType> getAllFundTypes() throws NotFoundException {
    List<FundType> fundTypes = fundTypeRepository.findAll();
    if (fundTypes.isEmpty()) {
      throw new NotFoundException("TeamMemberFund types could not found");
    }
    return fundTypes;
  }

  /** Get team fund for all members.
   *
   * @return team fund for all members
   * @throws NotFoundException when team fund is not found
   */
  public TeamFund getTeamFund() throws NotFoundException {
    List<Fund> funds = teamFundRepository.findAll();
    if (funds.isEmpty()) {
      throw new NotFoundException("TeamMemberFund types could not found");
    }
    return fundTransformer.transformToTeamFund(funds);
  }

  /** Save team fund.
   *
   * @param fundDTO fund dto received
   * @return Saved fund
   * @throws NotFoundException when any of the children is not found
   */
  public FundDTO saveTeamFund(FundDTO fundDTO) throws NotFoundException {
    Fund fund = fundTransformer.transformToFund(fundDTO);

    Optional<FundType> fundType = fundTypeRepository.findById(fundDTO.getType());

    if (!fundType.isPresent()) {
      String fundTypeNotFound = String.format("Fund type with id {} not found.", fundDTO.getId());
      throw new NotFoundException(fundTypeNotFound);
    }
    fund.setType(fundType.get());

    User addedBy = userService.findByUserName(fundDTO.getAddedBy());
    if (addedBy == null) {
      String addedByUserNotFound = "Added by user with username {} not found";;
      throw new NotFoundException(String.format(addedByUserNotFound, fundDTO.getAddedBy()));
    }
    fund.setAddedBy(addedBy);

    User owner = userService.findByUserName(fundDTO.getOwner());
    if (owner == null) {
      String ownerUserNotFound = "Owner user with username {} not found";
      throw new NotFoundException(String.format(ownerUserNotFound, fundDTO.getOwner()));
    }

    fund.setOwner(owner);

    return fundTransformer.transformToFundDTO(teamFundRepository.save(fund));
  }
}
