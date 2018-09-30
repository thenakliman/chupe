package org.thenakliman.chupe.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.properties.TokenProperty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTokenTest {

  @Mock
  private TokenProperty tokenProperty;

  @InjectMocks
  private UserToken userToken;

  //CHECKSTYLE.OFF: LineLength
  private final String tokenSigningKey = "5C062F492D34669254A4765371FED7A7DB27572758FDDBF5286AF4BC22487F23";
  private String token = "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJjaHVwZS1mcm9udGVuZCIsInN1YiI6ImxhbF9zaW5naCIsInJvbGVzIjpbIm1lbWJlciJdLCJuYW1lIjoiTGFsIFNpbmdoIiwiaXNzIjoiY2h1cGUiLCJleHAiOjE1MzgyOTM1ODYsImlhdCI6MTUzODI5Mjk4NiwiZW1haWwiOiJsYWxzaW5naEBleGFtcGxlLmNvbSIsInVzZXJuYW1lIjoibGFsX3NpbmdoIn0.DvaQKUB5GJ46GWFObWfgBlbXAJSLLDNdm8_a-T88djGfhhQuundy2Y4LAkucjsmiC1W62uCRRcShKqotPK_2rg";
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