package org.thenakliman.chupe.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TeamFund {
  private List<TeamMemberFund> teamMemberFunds;
}
