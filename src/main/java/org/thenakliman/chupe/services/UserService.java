package org.thenakliman.chupe.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;


@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
