package org.thenakliman.chupe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.services.UserService;

import java.util.List;


@Controller
public class UserController extends BaseController {
  @Autowired
  UserService userService;

  @GetMapping("/users")
  public ResponseEntity<List<UserDTO>> getUsers() {
    try {
      return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
