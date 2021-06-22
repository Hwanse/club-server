package com.study.clubserver.api.dto.club;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.api.dto.club.ClubAccountStatusDto;
import com.study.clubserver.api.dto.club.ClubDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubMembersDetailsDto {

  private ClubDto clubInfo;

  private ClubAccountStatusDto clubAccountStatus;

  private List<ClubAccountInfoDto> clubAccountsInfo;

}
