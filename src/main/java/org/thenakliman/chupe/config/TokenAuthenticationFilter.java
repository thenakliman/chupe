package org.thenakliman.chupe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.thenakliman.chupe.services.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private TokenAuthenticationService tokenAuthenticationService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    final String token = ((HttpServletRequest)request).getHeader("Authorization");
    if(token != null) {
      if(tokenService.isTokenValid(token)) {
        SecurityContextHolder.getContext().setAuthentication(tokenAuthenticationService.getAuthentication(token));
      }
    }

    chain.doFilter(request, response);
  }
}
