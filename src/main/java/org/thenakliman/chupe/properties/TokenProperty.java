package org.thenakliman.chupe.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProperty {
  @Value("${chupe.token.expirytime}")
  private int tokenExpiryTime;

  @Value("${chupe.token.issuer}")
  private String tokenIssuer;

  @Value("${chupe.token.audience}")
  private String tokenAudience;

  @Value("${chupe.token.signingkey}")
  private String tokenSigningKey;

  public int getTokenExpiryTime() {
    return tokenExpiryTime;
  }

  public String getTokenIssuer() {
    return tokenIssuer;
  }

  public String getTokenAudience() {
    return tokenAudience;
  }

  public String getTokenSigningKey() {
    return tokenSigningKey;
  }
}
