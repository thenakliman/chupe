package org.thenakliman.chupe.config;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.User;

@Component
public final class AuthenticatedUser implements Authentication {

  private final User user;

  public static AuthenticatedUser getAuthentication(User user) {
    return new AuthenticatedUser(user);
  }

  private AuthenticatedUser(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return false;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    /** to be filled in as per requirement */
  }

  @Override
  public String getName() {
    return user.getName();
  }
}
