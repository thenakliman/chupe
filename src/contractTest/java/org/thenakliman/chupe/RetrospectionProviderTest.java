package org.thenakliman.chupe;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.TargetRequestFilter;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import java.util.Collections;
import org.apache.http.HttpRequest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.thenakliman.chupe.config.UserToken;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.services.RetroService;
import org.thenakliman.chupe.services.TokenService;
import org.thenakliman.chupe.validators.RetroValidationService;


@RunWith(SpringRestPactRunner.class)
@Provider("chupe")
@PactBroker(host = "localhost", port = "80", protocol = "http", consumers = "chupe-frontend")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrospectionProviderTest {
  @TestTarget
  public final Target target = new SpringBootHttpTarget();

  @MockBean
  private RetroService retroService;

  @MockBean
  private RetroValidationService retroValidationService;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private UserToken userToken;

  @TargetRequestFilter
  public void exampleRequestFilter(HttpRequest request) {
    request.addHeader("Authorization", "token");
  }

  @Before
  public void setup() {
    when(tokenService.isTokenValid(anyString())).thenReturn(true);
    when(userToken.getUser(anyString())).thenReturn(User.builder().username("username").build());
  }

  @State("should return all retros")
  public void shouldReturnRetros() {
    RetroDTO retroDto = RetroDTO
        .builder()
        .createdBy("James")
        .id(101L)
        .maximumVote(3L)
        .status(RetroStatus.CREATED)
        .name("retro name")
        .build();

    when(retroService.getRetros()).thenReturn(Collections.singletonList(retroDto));
  }
}
