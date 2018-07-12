package org.thenakliman.chupe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.properties.TokenProperty;

import java.util.List;

@Component
public class UserToken {

  @Autowired
  private TokenProperty tokenProperty;

  /** Returns Authenticated user details.
   *
   * @param userToken received in the request
   * @return Authenticated user
   * @throws SignatureException if signature verification fails
   */
  public User getUser(String userToken) throws SignatureException {
    if (userToken == null) {
      return null;
    }

    User user = new User();
    byte[] key = TextCodec.BASE64.decode(tokenProperty.getTokenSigningKey());
    JwtParser jwtParser = Jwts.parser().setSigningKey(key);
    Claims jwtClaims = jwtParser.parseClaimsJws(userToken).getBody();

    user.setUsername(jwtClaims.get("username").toString());
    user.setEmail(jwtClaims.get("email").toString());
    user.setName(jwtClaims.get("name").toString());
    user.setRoles(jwtClaims.get("roles", List.class));

    return user;
  }
}
