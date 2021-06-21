package com.study.clubserver.domain.club;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.account.Account;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class ClubAccount extends CommonEntity {

  @ManyToOne
  @JoinColumn(name = "club_id",nullable = false)
  private Club club;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  private boolean isLiked;

  @OneToOne
  private ClubAccountRole clubAccountRole;

}
