package org.thenakliman.chupe.services;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;
import org.thenakliman.chupe.transformer.FundTransformer;

@Service
public class TeamFundService {
  @Autowired
  private TeamFundRepository teamFundRepository;

  @Autowired
  private FundTypeRepository fundTypeRepository;

  @Autowired
  private FundTransformer fundTransformer;

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
}
