package org.thenakliman.chupe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.thenakliman.chupe.dto.User;

public class UserToken {
  private String userToken;

  @Value("${chupe.token.signingkey}")
  private String tokenSigningKey;

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
    byte[] key = TextCodec.BASE64.decode(tokenSigningKey);
    JwtParser jwtParser = Jwts.parser().setSigningKey(key);
    Claims jwtClaims = jwtParser.parseClaimsJws(userToken).getBody();

    user.setUsername(jwtClaims.get("username").toString());
    user.setEmail(jwtClaims.get("email").toString());
    user.setName(jwtClaims.get("name").toString());
    user.setRoles(jwtClaims.get("roles", List.class));

    return user;
  }

  public String getUserToken() {
    return userToken;
  }

  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }
}
