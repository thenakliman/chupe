package org.thenakliman.chupe.dto;

import java.util.List;

public class TeamFund {
  private List<TeamMemberFund> teamMemberFunds;

  public List<TeamMemberFund> getTeamMemberFunds() {
    return teamMemberFunds;
  }

  public void setTeamMemberFunds(List<TeamMemberFund> teamMemberFunds) {
    this.teamMemberFunds = teamMemberFunds;
  }
}
