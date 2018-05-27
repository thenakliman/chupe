package org.thenakliman.chupe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thenakliman.chupe.services.TokenService;
import org.thenakliman.chupe.services.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  UserService userService;

  @Autowired
  TokenService tokenService;

  private DaoAuthenticationProvider getDaoAuthenticationProvider() {
    return new DaoAuthenticationProvider();
  }

  private BCryptPasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    DaoAuthenticationProvider daoAuthenticationProvider = getDaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
    daoAuthenticationProvider.setUserDetailsService(userService);
    auth.authenticationProvider(daoAuthenticationProvider);
  }

  protected void configure(HttpSecurity http) {
    try {
      http
          .authorizeRequests()
          .antMatchers("/api/v1/**").permitAll();
    } catch (Exception e) {
      // todo(thenakliman) Currently security is disabled
    }
  }
}
