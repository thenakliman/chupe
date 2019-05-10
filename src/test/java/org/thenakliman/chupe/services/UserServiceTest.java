package org.thenakliman.chupe.services;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.UserRepository;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Mock
  private Converter converter;

  @Test
  public void shouldReturnEmptyUser() {
    given(userRepository.findAll()).willReturn(emptyList());

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

    UserDTO expectedUserDTO1 = new UserDTO(
        "user1_username",
        "user1_first_name",
        "user1_last_name",
        "user1_email");

    UserDTO expectedUserDTO2 = new UserDTO(
        "user1_username",
        "user1_first_name",
        "user1_last_name",
        "user1_email");

    given(userRepository.findAll()).willReturn(repoUser);
    given(converter.convertToListOfObjects(repoUser, UserDTO.class))
        .willReturn(asList(expectedUserDTO1, expectedUserDTO2));

    List<UserDTO> users = userService.getAllUsers();

    assertThat(users, hasItems(expectedUserDTO1, expectedUserDTO2));
  }

  @Test
  public void shouldReturnUserByUsername() {
    User testUser = getUser();
    given(userRepository.findByUserName("user1_username")).willReturn(testUser);
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
    given(userRepository.findByUserName(testUser)).willReturn(null);
    userService.findByUserName(testUser);
  }

  @Test
  public void shouldReturnUserModel() {
    String testUser = "testUser";
    given(userRepository.findByUserName(testUser)).willReturn(getUser());
    User user = userService.findByUserName(testUser);
    assertThat(user, samePropertyValuesAs(getUser()));
  }
}