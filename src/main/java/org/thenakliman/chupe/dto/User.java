package org.thenakliman.chupe.dto;

import java.util.ArrayList;
import java.util.List;

public class User {

  private String username;

  private String email;

  private String name;

  private List<String> roles = new ArrayList<String>();

  /** Empty constructor. */
  public User() {
    /** For making it bean */
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
