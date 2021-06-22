package com.study.clubserver.domain.account;

import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.domain.CommonEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private AccountRole accountRole;

  public Account(JoinRequest joinRequest) {
    this.userId = joinRequest.getUserId();
    this.email = joinRequest.getEmail();
    this.password = joinRequest.getPassword();
    this.name = joinRequest.getName();
  }

  public Account(String userId, String email, String password, String name) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.name = name;
  }

  public void encodePassword(String encodedPassword) {
    this.password = encodedPassword;
  }

  public void role(AccountRole accountRole) {
    this.accountRole = accountRole;
  }

  public void deleteRole() {
    this.accountRole = null;
  }

}