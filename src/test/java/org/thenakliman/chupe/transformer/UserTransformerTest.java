package org.thenakliman.chupe.transformer;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import org.thenakliman.chupe.dto.UserDTO;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class UserTransformerTest {

  @InjectMocks
  private UserTransformer userTransformer;

  @Test
  public void shouldTranformUserListOfModelsToUserDTO() {
    List<User> user1 = new ArrayList<>();
    User u1 = new User("username1",
               "user1_firstname",
               "user1_lastname",
                "user1_email",
               "user1_password",
              true);
    user1.add(u1);

    UserDTO userDTO1 = new UserDTO();
    userDTO1.setUserName("username1");
    userDTO1.setFirstName("user1_firstname");
    userDTO1.setLastName("user1_lastname");
    userDTO1.setEmail("user1_email");

    List<UserDTO> userDTOs = new ArrayList<>();
    userDTOs.add(userDTO1);

    assertThat(userDTOs, samePropertyValuesAs(userTransformer.transformToUserDTOs(user1)));
  }

  @Test
  public void shouldReturnEmptyListIfNullIsGivenForTransformation() {
    assertThat(new ArrayList<>(), samePropertyValuesAs(userTransformer.transformToUserDTOs(null)));
  }

    @Test
    public void shouldTranformUserModelToUserDTO() {
        User u1 = new User("user1_firstname",
                "user1_lastname",
                "user1_username",
                "user1_email",
                "user1_password",
                true);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("user1_username");
        userDTO.setFirstName("user1_firstname");
        userDTO.setLastName("user1_lastname");
        userDTO.setEmail("user1_email");

        assertThat(userDTO, samePropertyValuesAs(userTransformer.transformToUserDTO(u1)));
    }
}