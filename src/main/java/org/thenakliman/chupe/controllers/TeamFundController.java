package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.dto.FundTypeDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.services.TeamFundService;


@Controller
public class TeamFundController extends BaseController {
  private TeamFundService teamFundService;

  public TeamFundController(TeamFundService teamFundService) {
    this.teamFundService = teamFundService;
  }

  @GetMapping("/team-funds/types")
  public ResponseEntity teamFundTypes(@RequestHeader HttpHeaders header) {
    List<FundTypeDTO> teamFundTypes;
    teamFundTypes = teamFundService.getAllFundTypes();
    return new ResponseEntity<>(teamFundTypes, HttpStatus.OK);
  }

  @GetMapping("/team-funds")
  public ResponseEntity<TeamFund> teamFund(@RequestHeader HttpHeaders header) {
    TeamFund teamFund = teamFundService.getTeamFund();
    return new ResponseEntity<>(teamFund, HttpStatus.OK);
  }

  @PostMapping("/team-funds")
  public ResponseEntity<FundDTO> saveFund(@RequestHeader HttpHeaders header,
                                          @RequestBody UpsertFundDTO fund) throws NotFoundException {
    return new ResponseEntity<>(teamFundService.saveTeamFund(fund, getRequestUsername()), HttpStatus.OK);
  }

  @GetMapping("/funds")
  public ResponseEntity<List<FundDTO>> getFundsForGivenUser(
      @RequestParam("owner") String owner) throws NotFoundException {
    return new ResponseEntity<>(teamFundService.getFundForATeamMember(owner), HttpStatus.OK);
  }
}
