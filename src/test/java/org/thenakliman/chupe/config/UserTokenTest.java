package org.thenakliman.chupe.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.properties.TokenProperty;


@RunWith(MockitoJUnitRunner.class)
public class UserTokenTest {

  //CHECKSTYLE.OFF: LineLength
  private final String tokenSigningKey = "testKey";
  @Mock
  private TokenProperty tokenProperty;
  @InjectMocks
  private UserToken userToken;
  private String token = "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJ0ZXN0QXVkaWVuY2UiLCJzdWIiOiJ1c2VybmFtZSIsInJvbGVzIjpbIm1lbWJlciJdLCJuYW1lIjoiTGFsIFNpbmdoIiwiaXNzIjoidGVzdElzc3VlciIsImV4cCI6NjE1Mzg0MDk2MTcsImlhdCI6MTUzODQwOTY3NywiZW1haWwiOiJsYWxzaW5naEBleGFtcGxlLmNvbSIsInVzZXJuYW1lIjoibGFsX3NpbmdoIn0.koTvlIeoOOrSNmdUCcOs4gY__urR1RibPndSEDPS7Vx_f3iZmg4_Sq4VTu2ugMEchG0q2T25PZfxkL3UEBWozg";
  //CHECKSTYLE.ON: LineLength

  private User getUser() {
    User user = new User();
    user.setEmail("lalsingh@example.com");
    user.setName("Lal Singh");
    user.setUsername("lal_singh");
    List<String> roles = new ArrayList<>();
    roles.add("member");
    user.setRoles(roles);
    return user;
  }

  @Test
  public void shouldReturnUser() {
    when(tokenProperty.getTokenSigningKey()).thenReturn(tokenSigningKey);

    User user = userToken.getUser(token);

    User expectedUser = getUser();
    assertEquals(expectedUser.getRoles(), user.getRoles());
    assertEquals(expectedUser.getUsername(), user.getUsername());
    assertEquals(expectedUser.getName(), user.getName());
    assertEquals(expectedUser.getEmail(), user.getEmail());
  }

  @Test
  public void shouldReturnNullIfEmptyToken() {
    userToken = new UserToken();
    assertNull(userToken.getUser(null));
  }
}