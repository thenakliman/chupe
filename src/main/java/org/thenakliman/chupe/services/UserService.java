package org.thenakliman.chupe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;


@Service
public class UserService implements UserDetailsService {
  private UserRepository userRepository;

  private ConverterUtil converterUtil;

  @Autowired
  public UserService(UserRepository userRepository, ConverterUtil converterUtil) {
    this.userRepository = userRepository;
    this.converterUtil = converterUtil;
  }

  public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return converterUtil.convertToListOfObjects(users, UserDTO.class);
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
