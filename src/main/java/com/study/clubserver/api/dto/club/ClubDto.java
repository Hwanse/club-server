package com.study.clubserver.api.dto.club;

import com.study.clubserver.domain.club.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClubDto {

  private Long id;

  private String title;

  private String description;

  private int memberCount;

  private int limitMemberCount;

  private String bannerImageUrl;

  public ClubDto(Club club) {
    this.id = club.getId();
    this.title = club.getTitle();
    this.description = club.getDescription();
    this.memberCount = club.getMemberCount();
    this.limitMemberCount = club.getLimitMemberCount();
    this.bannerImageUrl = club.getBannerImageUrl();
  }

}
