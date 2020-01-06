package org.thenakliman.chupe.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.thenakliman.chupe.models.User;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetail implements UserDetails {
  private final User user;

  public UserDetail(User user) {
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<GrantedAuthority>();
  }


  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
}
