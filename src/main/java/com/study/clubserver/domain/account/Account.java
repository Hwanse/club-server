package com.study.clubserver.domain.account;

import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.role.RoleType;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Account extends CommonEntity {

  @Column(unique = true, nullable = false, length = 100)
  private String userId;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 100)
  private String password;

  private String refreshToken;

  private boolean isJoined;

  @OneToOne(mappedBy = "account")
  private AccountRole accountRole;

  public Account(JoinRequest joinRequest) {
    this.userId = joinRequest.getUserId();
    this.email = joinRequest.getEmail();
    this.password = joinRequest.getPassword();
    this.name = joinRequest.getName();
  }

  public void encodePassword(String encodedPassword) {
    this.password = encodedPassword;
  }

  public void role(AccountRole accountRole) {
    this.accountRole = accountRole;
  }
}