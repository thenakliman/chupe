package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.FundTypeDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.mappings.FundTransformer;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;


@Service
public class TeamFundService {
  private TeamFundRepository teamFundRepository;
  private FundTypeRepository fundTypeRepository;
  private FundTransformer fundTransformer;
  private Converter converter;
  private UserService userService;

  @Autowired
  public TeamFundService(TeamFundRepository teamFundRepository,
                         FundTypeRepository fundTypeRepository,
                         FundTransformer fundTransformer,
                         Converter converter,
                         UserService userService) {

    this.teamFundRepository = teamFundRepository;
    this.fundTypeRepository = fundTypeRepository;
    this.fundTransformer = fundTransformer;
    this.converter = converter;
    this.userService = userService;
  }

  public List<FundTypeDTO> getAllFundTypes() {
    return converter.convertToListOfObjects(fundTypeRepository.findAll(), FundTypeDTO.class);
  }

  public TeamFund getTeamFund() {
    List<Fund> funds = teamFundRepository.findAll();
    return fundTransformer.transformToTeamFund(funds);
  }

  public FundDTO saveTeamFund(UpsertFundDTO fundDTO, String addedByUsername) throws NotFoundException {
    Fund fund = converter.convertToObject(fundDTO, Fund.class);

    Optional<FundType> fundType = fundTypeRepository.findById(fundDTO.getType());

    if (!fundType.isPresent()) {
      String fundTypeNotFound = format("Fund type with id %d not found.", fundDTO.getType());
      throw new NotFoundException(fundTypeNotFound);
    }
    fund.setType(fundType.get());
    fund.setAddedBy(User.builder().userName(addedByUsername).build());

    User owner = userService.findByUserName(fundDTO.getOwner());
    fund.setOwner(owner);
    return converter.convertToObject(teamFundRepository.save(fund), FundDTO.class);
  }

  public List<FundDTO> getFundForATeamMember(String username) {
    List<Fund> teamMemberFunds = teamFundRepository.findByOwnerUserName(username);
    return converter.convertToListOfObjects(teamMemberFunds, FundDTO.class);
  }
}
