package org.thenakliman.chupe.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.User;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticatedUserTest {

  private User getUser() {
    User user = new User();
    user.setEmail("lal_singh@gmail.com");
    user.setName("lal singh");
    user.setUsername("lal_singh");
    List<String> roles = new ArrayList<>();
    roles.add("admin");
    user.setRoles(roles);
    return user;
  }

  @Test
  public void shouldReturnName() {
    AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthentication(getUser());
    assertEquals("lal singh", authenticatedUser.getName());
  }

  @Test
  public void shouldReturnNullForWhenCalledGetCredentials() {
    AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthentication(getUser());
    assertNull(authenticatedUser.getCredentials());
  }

  @Test
  public void shouldReturnNullForWhenCalledGetPrincipal() {
    AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthentication(getUser());
    assertNull(authenticatedUser.getPrincipal());
  }

  @Test
  public void shouldReturnNullForWhenCalledGetAuthorities() {
    AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthentication(getUser());
    assertNull(authenticatedUser.getAuthorities());
  }
}