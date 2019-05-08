package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.services.TokenService;

@Controller
public class TokenController {
  @Autowired
  private TokenService tokenService;

  @GetMapping("token")
  public ResponseEntity<String> getToken(@RequestParam String username) {
    // todo(thenakliman): Handle if failure occurred while processing request.
    String token = tokenService.createToken(username);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Authorization", token);
    return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
  }
}
