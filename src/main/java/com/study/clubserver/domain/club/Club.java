package com.study.clubserver.domain.club;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.board.Board;
import com.study.clubserver.domain.interests.ClubInterests;
import com.study.clubserver.domain.interests.Interests;
import com.study.clubserver.domain.meeting.Meeting;
import com.study.clubserver.domain.zone.ClubZone;
import com.study.clubserver.domain.zone.Zone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Club extends CommonEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private int memberCount;

  private int limitMemberCount;

  @Lob
  private String bannerImageUrl;

  @OneToMany(mappedBy = "club")
  private List<ClubAccount> members = new ArrayList<>();

  @OneToMany(mappedBy = "club")
  private List<Meeting> meetings = new ArrayList<>();

  @OneToMany(mappedBy = "club")
  private Set<ClubInterests> interests = new HashSet<>();

  @OneToMany(mappedBy = "club")
  private Set<ClubZone> zones = new HashSet<>();

  @OneToMany(mappedBy = "club")
  private List<Board> boards = new ArrayList<>();

}
