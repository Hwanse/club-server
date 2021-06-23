package com.study.clubserver.api.dto.club;

import com.study.clubserver.domain.club.Club;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.text.DateFormatter;
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

  public ClubDto(Club club) {
    this.id = club.getId();
    this.title = club.getTitle();
    this.description = club.getDescription();
    this.memberCount = club.getMemberCount();
    this.limitMemberCount = club.getLimitMemberCount();
    this.bannerImageUrl = club.getBannerImageUrl();
    this.createdAt = club.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    this.updatedAt = club.getUpdatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
  }

}
