package org.thenakliman.chupe.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.models.Role;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.properties.TokenProperty;
import org.thenakliman.chupe.repositories.RoleRepository;
import org.thenakliman.chupe.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TokenService {
  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenProperty tokenProperties;

  private Map<String, Object> getClaims(String username) {

    User user = userRepository.findByUserName(username);
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("email", user.getEmail());
    claims.put("name", user.getFirstName() + " " + user.getLastname());
    claims.put("username", user.getUserName());

    List<Role> assignedRolesToUser = roleRepository.findByUsername(username);
    List roles = new ArrayList<String>();
    assignedRolesToUser.forEach(assignedRoleToUser -> roles.add(assignedRoleToUser.getRole()));

    claims.put("roles", roles);
    return claims;
  }

  /** Creates a token based on username. */
  public String createToken(String username) {
    byte[] key = tokenProperties.getTokenSigningKey().getBytes();
    DateUtil dateUtil = new DateUtil();
    return Jwts.builder()
        .setClaims(getClaims(username))
        .setIssuedAt(dateUtil.getTime())
        .setAudience(tokenProperties.getTokenAudience())
        .setIssuer(tokenProperties.getTokenIssuer())
        .setExpiration(
                dateUtil.addMinutes(dateUtil.getTime(),
                tokenProperties.getTokenExpiryTime()))
        .setSubject(username)
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();
  }

  /** Verify that a token is valid. */
  public boolean isTokenValid(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(tokenProperties.getTokenSigningKey().getBytes())
          .parseClaimsJws(token)
          .getBody()
          .getIssuer()
          .equals(tokenProperties.getTokenIssuer());
    } catch (MalformedJwtException e) {
      return false;
    }
  }
}
