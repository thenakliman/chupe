package org.thenakliman.chupe.transformer;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.TransactionType;


@Component
public class FundTransformer {

  /** Transform Fund to TeamFund.
   *
   * @param funds List of all funds transations
   * @return Team fund for each member
   */
  public TeamFund transformToTeamFund(List<Fund> funds) {
    TeamFund teamFund = new TeamFund();
    Map<String, TeamMemberFund> teamMemberFundMap = new HashMap<>();
    funds.forEach(fund -> {
      long transactionAmount = 0;
      if (fund.getTransactionType() == TransactionType.CREDIT) {
        transactionAmount = fund.getAmount();
      } else {
        transactionAmount = -fund.getAmount();
      }

      if (teamMemberFundMap.containsKey(fund.getOwner().getUserName())) {
        TeamMemberFund teamMemberFund = teamMemberFundMap.get(fund.getOwner().getUserName());
        teamMemberFund.setTotalAmount(teamMemberFund.getTotalAmount() + transactionAmount);
      } else {
        TeamMemberFund teamMemberFund = new TeamMemberFund(
            transactionAmount,
            fund.getOwner().getUserName(),
            fund.getTransactionType(),
            fund.isApproved());
        teamMemberFundMap.put(teamMemberFund.getOwner(), teamMemberFund);
      }
    });

    teamFund.setTeamMemberFunds(new ArrayList(teamMemberFundMap.values()));
    return teamFund;
  }

  /** Transform FundDTO to Fund model.
   *
   * @param fundDTO fund dto
   * @return Fund model
   */
  public Fund transformToFund(FundDTO fundDTO) {
    Fund fund = new Fund();
    fund.setId(fundDTO.getId());
    fund.setAmount(abs(fundDTO.getAmount()));
    fund.setApproved(fundDTO.isApproved());
    fund.setTransactionType(fundDTO.getTransactionType());
    return fund;
  }

  /** Transform FundDTO to Fund model.
   * @param fund to be transformed
   * @return fundDTO
   */
  public FundDTO transformToFundDTO(Fund fund) {
    return new FundDTO(
      fund.getId(),
      fund.getType().getId(),
      fund.getAmount(),
      fund.getOwner().getUserName(),
      fund.getAddedBy().getUserName(),
      fund.getTransactionType(),
      fund.isApproved()
    );
  }
}