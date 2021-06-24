package com.study.clubserver.api.dto.club;

import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubAccountInfoDto {

  private Long clubAccountId;

  private Long accountId;

  private String userId;

  private String userName;

  private String role;

  public ClubAccountInfoDto(ClubAccount clubAccount) {
    this.clubAccountId = clubAccount.getId();
    this.accountId = clubAccount.getAccount().getId();
    this.userId = clubAccount.getAccount().getUserId();
    this.userName = clubAccount.getAccount().getName();
    this.role = clubAccount.getClubAccountRole().getClubRole().getRoleType().getRoleName();
  }
}
