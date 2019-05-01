package org.thenakliman.chupe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.models.Role;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.properties.TokenProperty;
import org.thenakliman.chupe.repositories.RoleRepository;
import org.thenakliman.chupe.repositories.UserRepository;


@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

  private static final String username = "username";
  private static final String email = "example@test.com";
  private static final String audience = "testAudience";
  private static final String issuer = "testIssuer";
  private static final String key = "testKey";
  private static final String aRole = "admin";
  private static final String mRole = "member";
  @Mock
  RoleRepository roleRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  TokenProperty tokenProperties;
  @InjectMocks
  TokenService tokenService;

  private String getToken() {
    List<Role> roles = new ArrayList<Role>();
    Role memberRole = new Role("username", mRole);
    Role adminRole = new Role("username", aRole);
    roles.add(adminRole);
    roles.add(memberRole);
    String email = "example@test.com";
    User testUser = new User(
        "username",
        "password",
        email,
        "firstname",
        "lastname",
        true);

    BDDMockito.given(roleRepository.findByUsername(username)).willReturn(roles);
    BDDMockito.given(userRepository.findByUserName(username)).willReturn(testUser);

    BDDMockito.given(tokenProperties.getTokenIssuer()).willReturn(issuer);
    BDDMockito.given(tokenProperties.getTokenAudience()).willReturn(audience);
    BDDMockito.given(tokenProperties.getTokenExpiryTime()).willReturn(10);
    BDDMockito.given(tokenProperties.getTokenSigningKey()).willReturn(key);

    return tokenService.createToken(username);
  }


  @Test
  public void shouldCreateToken() {
    String token = getToken();
    byte[] keyBytes = key.getBytes();
    assertEquals(
        Jwts.parser().setSigningKey(keyBytes).parseClaimsJws(token).getBody().getAudience(),
        audience);

    assertEquals(
        Jwts.parser().setSigningKey(keyBytes).parseClaimsJws(token).getBody().getIssuer(),
        issuer);

    assertEquals(
        Jwts.parser().setSigningKey(keyBytes).parseClaimsJws(token).getBody().getSubject(),
        username);

    DefaultClaims tokenClaims = ((DefaultClaims) Jwts.parser().setSigningKey(keyBytes)
        .parse(token).getBody());

    assertEquals("firstname lastname", tokenClaims.get("name"));
    assertEquals(username, tokenClaims.get("username"));
    assertEquals(email, tokenClaims.get("email"));
    List roleList = new ArrayList();
    roleList.add(aRole);
    roleList.add(mRole);
    assertEquals(roleList, tokenClaims.get("roles"));
  }

  @Test
  public void shouldRaiseSignatureExceptionIfInvalidToken() {
    String token = getToken();
    token = token.substring(0, 4) + 'x' + token.substring(5);
    assertFalse(tokenService.isTokenValid(token));
  }

  @Test
  public void shouldReturnTrueForValidToken() {
    String token = getToken();
    assertTrue(tokenService.isTokenValid(token));
  }
}