package org.thenakliman.chupe.services;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    BDDMockito.given(userTransformer.transformToUserDTOs(null))
            .willReturn(new ArrayList<UserDTO>());
    List<UserDTO> users = userService.getAllUsers();
    assertEquals(new ArrayList(), users);
  }

  @Test
  public void shouldReturnUser() {
    User testUser1 = getUser();

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

  @Test
  public void shouldReturnUserByUsername() {
    User testUser = getUser();
    BDDMockito.given(userRepository.findByUserName("user1_username")).willReturn(testUser);
    UserDetail user = userService.loadUserByUsername("user1_username");
    assertEquals(testUser.getUserName(), user.getUsername());
  }

  private User getUser() {
    return new User(
              "user1_fist_name",
              "user1_last_name",
              "user1_username",
              "user1_email",
              "user1_password",
              true);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldRaiseUserNotFoundException() {
    String testUser = "testUser";
    BDDMockito.given(userRepository.findByUserName(testUser)).willReturn(null);
    userService.findByUserName(testUser);
  }

  @Test
  public void shouldReturnUserModel() {
    String testUser = "testUser";
    BDDMockito.given(userRepository.findByUserName(testUser)).willReturn(getUser());
    User user = userService.findByUserName(testUser);
    assertThat(user, samePropertyValuesAs(getUser()));
  }
}