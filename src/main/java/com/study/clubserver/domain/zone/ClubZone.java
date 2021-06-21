package com.study.clubserver.domain.zone;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.Club;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "club_zone")
@Getter
public class ClubZone extends CommonEntity {

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @ManyToOne
  @JoinColumn(name = "zone_id")
  private Zone zone;

}
