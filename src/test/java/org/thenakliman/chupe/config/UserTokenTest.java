package org.thenakliman.chupe.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.properties.TokenProperty;

@RunWith(MockitoJUnitRunner.class)
public class UserTokenTest {

  @Mock
  private TokenProperty tokenProperty;

  @InjectMocks
  private UserToken userToken;

  //CHECKSTYLE.OFF: LineLength
  private final String tokenSigningKey = "5C062F492D34669254A4765371FED7A7DB27572758FDDBF5286AF4BC22487F23";
  private String token = "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJjaHVwZS1mcm9udGVuZCIsInN1YiI6ImxhbF9zaW5naCIsInJvbGVzIjpbIm1lbWJlciJdLCJuYW1lIjoiTGFsIFNpbmdoIiwiaXNzIjoiY2h1cGUiLCJpYXQiOjE1MzA5NzgwMzksImVtYWlsIjoibGFsc2luZ2hAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6ImxhbF9zaW5naCJ9.b2QGyz9CDh2h_Ad1L88yXRG_pIWHKiObgCWKrEyXSZkg0GVvj_kBRDJawE9WfS2L738asEDjrfGeGruTTvdAPw";
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