package com.study.clubserver.domain.account;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.ClubAccount;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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

  @OneToMany(mappedBy = "account")
  private Set<AccountRole> accountRole = new HashSet<>();

}