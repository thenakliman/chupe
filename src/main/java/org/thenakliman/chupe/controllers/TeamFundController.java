package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.thenakliman.chupe.dto.QuestionDTO;
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
    List<FundType> teamFundFundTypes;
    try {
      teamFundFundTypes = teamFundService.getAllFundTypes();
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(teamFundFundTypes, HttpStatus.OK);
  }
}
