package org.thenakliman.chupe.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;

@Component
public class UserTransformer {

  /** Transforms Users Model into UserDTO for the response. */
  public List<UserDTO> transformToUserDTO(List<User> users) {
    List<UserDTO> usersDTOs = new ArrayList<UserDTO>();
    users.forEach(user -> {
      usersDTOs.add(new UserDTO(
              user.getUserName(),
              user.getFirstName(),
              user.getLastname(),
              user.getEmail()));
    });

    return usersDTOs;
  }
}
