package org.thenakliman.chupe.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.RetroPointService;

@Controller
public class RetroPointController extends BaseController {
  private final RetroPointService retroPointService;

  @Autowired
  public RetroPointController(RetroPointService retroPointService) {
    this.retroPointService = retroPointService;
  }

  @GetMapping("/retro-points")
  public ResponseEntity getRetroPoints(@RequestParam("retroId") long id) {
    List<RetroPointDTO> retroPoints = retroPointService.getRetroPoints(id);
    return new ResponseEntity<>(retroPoints, HttpStatus.OK);
  }

  @PostMapping("/retro-points")
  @PreAuthorize("@retroValidationService.isRetroOpen(#upsertRetroPointDTO.retroId)")
  public ResponseEntity createRetroPoint(@RequestBody @Valid UpsertRetroPointDTO upsertRetroPointDTO) {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    RetroPointDTO retroPoint = retroPointService.saveRetroPoint(
        upsertRetroPointDTO,
        userDetails.getUsername());

    return new ResponseEntity<>(retroPoint, HttpStatus.CREATED);
  }

  @PutMapping("/retro-points/{id}")
  @PreAuthorize("@retroValidationService.canBeUpdated(#retroPointId)")
  public ResponseEntity<RetroPointDTO> updateRetroPoint(@Valid @RequestBody UpsertRetroPointDTO retroPointDTO,
                                                        @PathVariable(value = "id") long retroPointId) {

    RetroPointDTO updatedRetroPoint = retroPointService.updateRetroPoint(retroPointId, retroPointDTO);
    return new ResponseEntity<>(updatedRetroPoint, HttpStatus.OK);
  }
}
