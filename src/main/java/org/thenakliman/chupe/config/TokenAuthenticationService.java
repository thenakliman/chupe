package org.thenakliman.chupe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.User;


@Service
public class TokenAuthenticationService {
  @Autowired
  private UserToken userToken;

  public Authentication getAuthentication(String token) {
    User user = userToken.getUser(token);
    return AuthenticatedUser.getAuthentication(user);
  }
}