package com.study.clubserver.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

  String userId() default "hwanse";

  String name() default "hwanse";

  String email() default "hwanse@email.com";

  String role() default "USER";

}