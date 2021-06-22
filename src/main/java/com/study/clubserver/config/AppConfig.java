package com.study.clubserver.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.clubserver.domain.role.RoleService;
import com.study.clubserver.domain.role.RoleType;
import java.util.Arrays;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements ApplicationRunner {

  private final RoleService roleService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Arrays.stream(RoleType.values())
          .forEach(roleType -> roleService.addRole(roleType));
  }

}
