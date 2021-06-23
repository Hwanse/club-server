package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.board.Board;
import com.study.clubserver.domain.interest.ClubInterest;
import com.study.clubserver.domain.meeting.Meeting;
import com.study.clubserver.domain.zone.ClubZone;
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

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ClubAccount> members = new ArrayList<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Meeting> meetings = new ArrayList<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ClubInterest> interests = new HashSet<>();

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
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
