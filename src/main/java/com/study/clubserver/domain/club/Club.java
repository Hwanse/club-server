package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubCreateRequest;
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Club extends CommonEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private int memberCount;

  private int limitMemberCount;

  @Lob
  private String bannerImageUrl;

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private List<ClubAccount> members = new ArrayList<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private List<Meeting> meetings = new ArrayList<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private Set<ClubInterests> interests = new HashSet<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private Set<ClubZone> zones = new HashSet<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private List<Board> boards = new ArrayList<>();

  public Club(ClubCreateRequest request) {
    this.title = request.getTitle();
    this.description = request.getDescription();
    this.limitMemberCount = request.getLimitMemberCount();
    this.bannerImageUrl = request.getBannerImageUrl();
  }

  public void incrementMemberCount() {
    this.memberCount++;
  }

}
