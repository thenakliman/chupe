package org.thenakliman.chupe.mappings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.TeamMemberFund;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.services.UserService;


@Component
public class FundTransformer {
  private UserService userService;

  @Autowired
  public FundTransformer(UserService userService) {
    this.userService = userService;
  }

  public TeamFund transformToTeamFund(List<Fund> funds) {
    Map<String, Long> teamMemberFundsMap = getFundMapForTeamMembers(funds);

    List<TeamMemberFund> teamMemberFunds = teamMemberFundsMap
        .keySet()
        .stream()
        .map(fundOwner -> TeamMemberFund
            .builder()
            .owner(fundOwner)
            .totalAmount(teamMemberFundsMap.get(fundOwner))
            .build())
        .collect(Collectors.toList());

    return TeamFund.builder()
        .teamMemberFunds(teamMemberFunds)
        .build();
  }

  private Map<String, Long> getFundMapForTeamMembers(List<Fund> funds) {
    Map<String, Long> teamMemberFundsMap = funds
        .stream()
        .collect(
            Collectors.groupingBy(fund -> fund.getOwner().getUserName(),
                Collectors.summingLong(this::getAmount)));

    Map<String, Long> userToFundMap = userService
        .getAllUsers()
        .stream()
        .collect(Collectors.toMap(UserDTO::getUserName, (user) -> 0L));

    return Stream.of(teamMemberFundsMap, userToFundMap)
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (a, b) -> a));
  }

  private long getAmount(Fund fund) {
    if (TransactionType.DEBIT.equals(fund.getTransactionType())) {
      return -fund.getAmount();
    }
    return fund.getAmount();
  }
}