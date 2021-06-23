package com.study.clubserver.api.dto.zone;

import com.study.clubserver.domain.zone.ClubZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDto {

  private Long id;

  private String name;

  private Long parentZoneId;

  private int level;

  public ZoneDto(ClubZone clubZone) {
    this.id = clubZone.getZone().getId();
    this.name = clubZone.getZone().getName();
    this.parentZoneId = clubZone.getZone().getParentZone().getId();
    this.level = clubZone.getZone().getLevel();
  }
}
