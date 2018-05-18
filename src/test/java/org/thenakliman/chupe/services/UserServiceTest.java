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

import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;
import org.thenakliman.chupe.transformer.UserTransformer;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserTransformer userTransformer;

  @InjectMocks
  UserService userService;

  @Test
  public void shouldReturnEmptyUser() {
    BDDMockito.given(userRepository.findAll()).willReturn(null);
    List<UserDTO> users = userService.getAllUsers();
    assertEquals(null, users);
  }

  @Test
  public void shouldReturnUser() {
    User testUser1 = new User(
            "user1_fist_name",
            "user1_last_name",
            "user1_username",
            "user1_email",
            "user1_password",
            true);

    User testUser2 = new User(
            "user2_fist_name",
            "user2_last_name",
            "user2_username",
            "user2_email",
            "user2_password",
            false);

    ArrayList<User> repoUser = new ArrayList<>();
    repoUser.add(testUser1);
    repoUser.add(testUser2);

    List<UserDTO> expUsers = new ArrayList<>();
    expUsers.add(new UserDTO(
            "user1_username",
            "user1_first_name",
            "user1_last_name",
            "user1_email"));

    expUsers.add(new UserDTO(
            "user1_username",
            "user1_first_name",
            "user1_last_name",
            "user1_email"));

    BDDMockito.given(userRepository.findAll()).willReturn(repoUser);
    BDDMockito.given(userTransformer.transformToUserDTOs(repoUser)).willReturn(expUsers);

    List<UserDTO> users = userService.getAllUsers();

    assertEquals(expUsers, users);
  }
}