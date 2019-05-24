package org.thenakliman.chupe.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.services.RetroService;


@Controller
public class RetroController extends BaseController {
  private final RetroService retroService;

  public RetroController(RetroService retroService) {
    this.retroService = retroService;
  }

  @GetMapping("/retros")
  public ResponseEntity getRetros() {
    List<RetroDTO> retros = retroService.getRetros();
    return new ResponseEntity<>(retros, HttpStatus.OK);
  }

  @PostMapping("/retros")
  public ResponseEntity createRetro(@RequestBody @Valid UpsertRetroDTO upsertRetroDTO) {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    RetroDTO retro = retroService.saveRetro(upsertRetroDTO, userDetails.getUsername());
    return new ResponseEntity<>(retro, HttpStatus.CREATED);
  }

  @PutMapping("/retros/{id}")
  public ResponseEntity<RetroDTO> updateRetro(@Valid @RequestBody UpsertRetroDTO upsertRetroDTO,
                                              @PathVariable(value = "id") long id) {
    RetroDTO updatedRetro = retroService.updateRetro(id, upsertRetroDTO);
    return new ResponseEntity<>(updatedRetro, HttpStatus.OK);
  }
}
