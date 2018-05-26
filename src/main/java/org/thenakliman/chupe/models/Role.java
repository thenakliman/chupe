package org.thenakliman.chupe.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name = "authorities")
@Table
public class Role implements Serializable {
  @Id
  @GeneratedValue
  private long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "authority", nullable = false)
  private String role;

  /** Empty constructor. */
  public Role() {
    /** String needs empty constructor **/
  }

  public Role(String username, String role) {
    this.username = username;
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public long getId() {
    return id;
  }
}