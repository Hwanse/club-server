package com.study.clubserver.config.security;

import com.study.clubserver.security.Jwt;
import com.study.clubserver.security.JwtAccessDenied;
import com.study.clubserver.security.JwtAuthenticationEntryPoint;
import com.study.clubserver.security.JwtAuthenticationFilter;
import com.study.clubserver.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAccessDenied jwtAccessDenied;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final Jwt jwt;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
       .antMatchers("/h2-console/**")
       .mvcMatchers("/join", "/login")
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
      .headers().frameOptions().disable()
      .and()
      .authorizeRequests()
        .anyRequest().authenticated()
      .and()
      .addFilterBefore(new JwtAuthenticationFilter(jwt), UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDenied);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jwtAuthenticationProvider());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider();
  }

}
