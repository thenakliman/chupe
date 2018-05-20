package org.thenakliman.chupe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;
import org.thenakliman.chupe.transformer.UserTransformer;


@Service
public class UserService implements  UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTransformer userTransformer;

  /** Fetch all the users from the repository. */
  public List<UserDTO> getAllUsers() {
    return userTransformer.transformToUserDTOs(userRepository.findAll());
  }

  @Override
  public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);
    if (user == null) {
      new UsernameNotFoundException(username);
    }
    return new UserDetail(user);
  }
}
