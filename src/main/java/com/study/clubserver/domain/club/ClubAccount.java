package com.study.clubserver.domain.club;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.account.Account;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ClubAccount extends CommonEntity {

  @ManyToOne
  @JoinColumn(name = "club_id",nullable = false)
  private Club club;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @OneToOne(mappedBy = "clubAccount")
  private ClubAccountRole clubAccountRole;

  public ClubAccount(Club club, Account account) {
    this.club = club;
    this.account = account;
    club.getMembers().add(this);
  }

  public void clubAccountRole(ClubAccountRole clubAccountRole) {
    this.clubAccountRole = clubAccountRole;
  }
}
