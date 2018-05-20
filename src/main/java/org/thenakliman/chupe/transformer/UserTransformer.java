package org.thenakliman.chupe.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;

@Component
public class UserTransformer {

  /** Transforms Users Model into UserDTO for the response. */
  public List<UserDTO> transformToUserDTOs(List<User> users) {
    List<UserDTO> usersDTOs = new ArrayList<UserDTO>();
    if (users == null) {
      return usersDTOs;
    }

    users.forEach(user -> usersDTOs.add(transformToUserDTO(user)));
    return usersDTOs;
  }

  /** Transforms User to UserDTO. */
  public UserDTO transformToUserDTO(User user) {
    return new UserDTO(
            user.getUserName(),
            user.getFirstName(),
            user.getLastname(),
            user.getEmail());
  }
}
