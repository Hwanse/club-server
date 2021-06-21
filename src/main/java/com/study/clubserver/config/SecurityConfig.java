package com.study.clubserver.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
       .mvcMatchers("/h2/**")
       .mvcMatchers("/docs/index.html")
       .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .formLogin().disable()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      // h2-console 은 iframe 을 사용하기 때문에 해당 옵션 비활성화
      .headers().frameOptions().disable();
  }

}
