package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.dto.TeamFund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.services.TeamFundService;


@Controller
public class TeamFundController  extends BaseController {
  @Autowired
  private TeamFundService teamFundService;

  /** API for fetching all type of team fund.
   * @return list of team fund types
   */
  @GetMapping("/team-funds/type")
  public ResponseEntity<QuestionDTO> teamFundTypes(@RequestHeader HttpHeaders header) {
    List<FundType> teamFundTypes;
    try {
      teamFundTypes = teamFundService.getAllFundTypes();
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(teamFundTypes, HttpStatus.OK);
  }

  /** API for fetching a team fund.
   * @return list of teamFund
   */
  @GetMapping("/team-funds")
  public ResponseEntity<TeamFund> teamFund(@RequestHeader HttpHeaders header) {
    TeamFund teamFund = teamFundService.getTeamFund();
    return new ResponseEntity(teamFund, HttpStatus.OK);
  }

  /** API for fetching a team fund.
   * @return list of teamFund
   */
  @PostMapping("/team-funds")
  public ResponseEntity<FundDTO> saveFund(@RequestHeader HttpHeaders header,
                                       @RequestBody FundDTO fund) throws NotFoundException {
    return new ResponseEntity(teamFundService.saveTeamFund(fund), HttpStatus.OK);
  }

  /** API for fetching a fund for a user.
   * @return list of fundDTOs
   */
  @GetMapping("/funds")
  public ResponseEntity<List<FundDTO>> getFundsForGivenUser(
      @RequestParam("owner") String owner) throws NotFoundException {
    try {
      return new ResponseEntity<>(teamFundService.getAllFundFor(owner), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
