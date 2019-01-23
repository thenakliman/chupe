package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.services.RetroService;


@Controller
public class RetroController extends BaseController {
  private RetroService retroService;

  public RetroController(RetroService retroService) {
    this.retroService = retroService;
  }

  @GetMapping("/retros")
  public ResponseEntity getRetros() {
    List<RetroDTO> retros = retroService.getRetros();
    return new ResponseEntity<>(retros, HttpStatus.OK);
  }

  @PostMapping("/retros")
  public ResponseEntity createRetro(@RequestBody RetroDTO retroDTO) {
    RetroDTO retro = retroService.saveRetro(retroDTO);
    return new ResponseEntity<>(retro, HttpStatus.CREATED);
  }

  @PutMapping("/retros/{id}")
  public ResponseEntity<RetroDTO> updateRetro(@RequestBody RetroDTO retroDTO,
                                            @PathVariable(value = "id") long id) {
    RetroDTO updatedRetro;

    try {
      updatedRetro = retroService.updateRetro(id, retroDTO);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(updatedRetro, HttpStatus.OK);
  }
}
