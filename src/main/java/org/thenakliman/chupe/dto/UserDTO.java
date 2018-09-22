package org.thenakliman.chupe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** UserDTO class for response. */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
  private String userName;

  private String firstName;

  private String lastName;

  private String email;
}
