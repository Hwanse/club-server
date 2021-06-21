package com.study.clubserver.security;

import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

  @Override
  public SecurityContext createSecurityContext(
    WithMockJwtAuthentication annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    JwtAuthenticationToken authentication =
      new JwtAuthenticationToken(
        new JwtAuthentication(annotation.userId(), annotation.email(), annotation.name(),
                              annotation.role()),
        null,
        Set.of(new SimpleGrantedAuthority(annotation.role()))
      );
    context.setAuthentication(authentication);
    return context;
  }

}