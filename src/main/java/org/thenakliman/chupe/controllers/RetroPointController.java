package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.services.RetroPointService;

@Controller
public class RetroPointController extends BaseController {
  private RetroPointService retroPointService;

  @Autowired
  public RetroPointController(RetroPointService retroPointService) {
    this.retroPointService = retroPointService;
  }

  @GetMapping("/retro-points")
  public ResponseEntity getRetros(@RequestParam("retroId") long id) {
    List<RetroPointDTO> retroPoints = retroPointService.getRetroPoints(id);
    return new ResponseEntity<>(retroPoints, HttpStatus.OK);
  }

  @PostMapping("/retro-points")
  public ResponseEntity createRetro(@RequestBody RetroPointDTO retroPointDTO) {
    RetroPointDTO retroPoint = retroPointService.saveRetroPoint(retroPointDTO);
    return new ResponseEntity<>(retroPoint, HttpStatus.CREATED);
  }

  @PutMapping("/retro-points/{id}")
  public ResponseEntity<RetroPointDTO> updateRetro(@RequestBody RetroPointDTO retroPointDTO,
                                              @PathVariable(value = "id") long id) {
    RetroPointDTO updatedRetroPoint;

    try {
      updatedRetroPoint = retroPointService.updateRetroPoint(id, retroPointDTO);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(updatedRetroPoint, HttpStatus.OK);
  }
}
