package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.FundTypeDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.services.TeamFundService;

import java.util.List;


@Controller
public class TeamFundController extends BaseController {
  private TeamFundService teamFundService;

  @Autowired
  public TeamFundController(TeamFundService teamFundService) {
    this.teamFundService = teamFundService;
  }

  @GetMapping("/team-funds/types")
  public ResponseEntity teamFundTypes() {
    List<FundTypeDTO> teamFundTypes = teamFundService.getAllFundTypes();
    return new ResponseEntity<>(teamFundTypes, HttpStatus.OK);
  }

  @GetMapping("/team-funds")
  public ResponseEntity<TeamFund> teamFund() {
    TeamFund teamFund = teamFundService.getTeamFund();
    return new ResponseEntity<>(teamFund, HttpStatus.OK);
  }

  @PostMapping("/team-funds")
  public ResponseEntity<FundDTO> saveFund(@RequestBody UpsertFundDTO fund) {
    return new ResponseEntity<>(teamFundService.saveTeamFund(fund, getRequestUsername()), HttpStatus.CREATED);
  }

  @GetMapping("/funds")
  public ResponseEntity<List<FundDTO>> getFundsForGivenUser(@RequestParam("owner") String owner) {
    return new ResponseEntity<>(teamFundService.getFundForATeamMember(owner), HttpStatus.OK);
  }
}
