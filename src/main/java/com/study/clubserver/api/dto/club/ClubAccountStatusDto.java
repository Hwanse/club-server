package com.study.clubserver.api.dto.club;

import com.study.clubserver.domain.club.ClubAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubAccountStatusDto {

  private Long clubAccountId;

  private Long accountId;

  private String userId;

  private String role;

  public ClubAccountStatusDto(ClubAccount clubAccount) {
    this.clubAccountId = clubAccount.getId();
    this.accountId = clubAccount.getAccount().getId();
    this.userId = clubAccount.getAccount().getUserId();
    this.role = clubAccount.getClubAccountRole().getClubRole().getRoleType().getRoleName();
  }
}
