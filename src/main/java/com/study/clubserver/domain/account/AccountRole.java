package com.study.clubserver.domain.account;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRole extends CommonEntity {

  @OneToOne
  private Account account;

  @Column(nullable = false)
  private String roleName;

  public AccountRole(Account account, String roleName) {
    this.account = account;
    this.roleName = roleName;
    account.role(this);
  }
}
