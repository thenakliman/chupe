package org.thenakliman.chupe.mappings;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.TransactionType;


@Component
public class FundTransformer {

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
}