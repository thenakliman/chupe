package org.thenakliman.chupe.services;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Test
  public void shouldReturnEmptyUser() {
    BDDMockito.given(userRepository.findAll()).willReturn(null);
    List<User> users = userService.getAllUsers();
    assertEquals(null, users);
  }

  @Test
  public void shouldReturnUser() {
    User testUser1 = new User("user1_fist_name", "user1_last_name", "user1_username",
                        "user1_email", "user1_password", true);
    User testUser2 = new User("user2_fist_name", "user2_last_name", "user2_username",
                        "user2_email", "user2_password", false);
    List<User> expUsers = new ArrayList<User>();
    expUsers.add(testUser1);
    expUsers.add(testUser2);
    BDDMockito.given(userRepository.findAll()).willReturn(expUsers);
    List<User> users = userService.getAllUsers();
    assertEquals(expUsers, users);
  }
}