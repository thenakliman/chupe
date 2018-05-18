package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.repositories.UserRepository;
import org.thenakliman.chupe.transformer.UserTransformer;

import java.util.List;


@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTransformer userTransformer;

  /** Fetch all the users from the repository. */
  public List<UserDTO> getAllUsers() {
    return userTransformer.transformToUserDTOs(userRepository.findAll());
  }
}
