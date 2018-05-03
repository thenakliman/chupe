package org.thenakliman.chupe.dto;

/** UserDTO class for response. */
public class UserDTO {
  private String userName;
  private String firstName;
  private String lastName;
  private String email;

  public UserDTO() {
  /** It has been kept for spring to make empty object and then use setters. */
  }

  /** Constructore for creating UserDTO. */
  public UserDTO(String userName, String firstName, String lastName, String email) {
    this.userName = userName;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
