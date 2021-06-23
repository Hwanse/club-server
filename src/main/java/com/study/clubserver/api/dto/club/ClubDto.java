package com.study.clubserver.api.dto.club;

import com.study.clubserver.api.dto.interest.InterestDto;
import com.study.clubserver.api.dto.zone.ZoneDto;
import com.study.clubserver.domain.club.Club;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Set;
import java.util.stream.Collectors;
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

  private String createdAt;

  private String updatedAt;

  private Set<InterestDto> interests;

  private Set<ZoneDto> zones;

  public ClubDto(Club club) {
    this.id = club.getId();
    this.title = club.getTitle();
    this.description = club.getDescription();
    this.memberCount = club.getMemberCount();
    this.limitMemberCount = club.getLimitMemberCount();
    this.bannerImageUrl = club.getBannerImageUrl();
    this.createdAt = club.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    this.updatedAt = club.getUpdatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    this.interests = club.getInterests().stream().map(InterestDto::new).collect(Collectors.toSet());
    this.zones = club.getZones().stream().map(ZoneDto::new).collect(Collectors.toSet());
  }

}
