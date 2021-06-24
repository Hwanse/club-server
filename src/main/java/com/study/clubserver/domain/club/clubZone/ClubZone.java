package com.study.clubserver.domain.club.clubZone;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.zone.Zone;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club_zone")
@Getter
@NoArgsConstructor
public class ClubZone extends CommonEntity {

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @ManyToOne
  @JoinColumn(name = "zone_id")
  private Zone zone;

  public ClubZone(Club club, Zone zone) {
    this.club = club;
    this.zone = zone;
    club.getZones().add(this);
  }
}
