package org.thenakliman.chupe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;


@Service
public class UserService implements UserDetailsService {
  private UserRepository userRepository;

  private Converter converter;

  @Autowired
  public UserService(UserRepository userRepository, Converter converter) {
    this.userRepository = userRepository;
    this.converter = converter;
  }

  public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return converter.convertToListOfObjects(users, UserDTO.class);
  }

  @Override
  public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new UserDetail(user);
  }

  User findByUserName(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return user;
  }
}
