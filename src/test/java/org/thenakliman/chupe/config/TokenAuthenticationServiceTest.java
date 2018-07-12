package org.thenakliman.chupe.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.thenakliman.chupe.dto.User;

@RunWith(MockitoJUnitRunner.class)
public class TokenAuthenticationServiceTest {

  @Mock
  private UserToken userToken;

  @InjectMocks
  private TokenAuthenticationService tokenAuthenticationService;

  @Test
  public void shouldReturnAuthenticatedUser() {
    String token = "fakeToken";
    User user = new User();
    String testUser = "testUser";
    user.setName(testUser);
    BDDMockito.given(userToken.getUser(token)).willReturn(user);
    Authentication authentication = tokenAuthenticationService.getAuthentication(token);
    assertEquals(authentication.getName(), testUser);
  }
}