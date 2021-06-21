package com.study.clubserver.domain.account;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class AccountRole extends CommonEntity {

  @ManyToOne
  private Account account;

  @Column(nullable = false)
  private String roleName;

}
