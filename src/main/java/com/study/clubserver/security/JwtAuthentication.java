package com.study.clubserver.security;

import com.study.clubserver.domain.account.Account;
import lombok.Getter;

@Getter
public class JwtAuthentication {

  private final String userId;

  private final String email;

  private final String username;

  private final String roles;

  public JwtAuthentication(String userId, String email, String username, String roles) {
    this.userId = userId;
    this.email = email;
    this.username = username;
    this.roles = roles;
  }

  public JwtAuthentication(Account account) {
    this(account.getUserId(), account.getEmail(), account.getName()
        , account.getAccountRole().getRoleName());
  }

}
