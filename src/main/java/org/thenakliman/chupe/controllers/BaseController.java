package org.thenakliman.chupe.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thenakliman.chupe.dto.User;

@Controller
@RequestMapping("/api/v1")
public class BaseController {
  String getRequestUsername() {
    User userDetails =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUsername();
  }
}
