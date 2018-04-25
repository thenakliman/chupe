package org.thenakliman.chupe.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "users")
@Table
public class User {
  @Id
  @Column(name = "username", nullable = false, unique = true)
  private String userName;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastname;

  @Column(name = "enabled")
  private boolean enabled;

  /** Empty constructor. */
  public User() {
    /** NOTE(thenakliman): Empty constructor is needed to make it bean.
     *  Seems like, spring create an empty constructor and then set attribute
     *  using setters.
     */
  }

  /** Constructor to build object. */
  public User(String firstName, String lastName,
              String userName, String email,
              String password, boolean enabled) {

    this.firstName = firstName;
    this.lastname = lastName;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.enabled = enabled;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
