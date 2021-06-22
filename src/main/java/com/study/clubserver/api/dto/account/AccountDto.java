package com.study.clubserver.api.dto.account;

import com.study.clubserver.domain.account.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

  private Long accountId;

  private String userId;

  private String email;

  private String name;

  private String roles;

  public AccountDto(Account account) {
    this.userId = account.getUserId();
    this.email = account.getEmail();
    this.name = account.getName();
    this.roles = account.getAccountRole().getRoleName();
  }
}
