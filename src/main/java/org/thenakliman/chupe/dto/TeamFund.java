package org.thenakliman.chupe.dto;

import java.util.List;
import lombok.Data;


@Data
public class TeamFund {
  private List<TeamMemberFund> teamMemberFunds;
}
