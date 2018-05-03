package org.thenakliman.chupe.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;
import org.thenakliman.chupe.transformer.UserTransformer;


@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTransformer userTransformer;

  /** Fetch all the users from the repository. */
  public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();

    if (users == null) {
      return null;
    }

    return userTransformer.transformToUserDTO(users);
  }
}
