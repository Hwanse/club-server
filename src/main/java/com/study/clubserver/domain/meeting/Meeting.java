package com.study.clubserver.domain.meeting;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubAccount;
import com.study.clubserver.domain.zone.Zone;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Meeting extends CommonEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private LocalDateTime meetingStartTime;

  private LocalDateTime meetingEndTime;

  private int entryCount;

  private int limitEntryCount;

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @OneToOne
  @JoinColumn(name = "zone_id")
  private Zone zone;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "participant")
  private List<MeetingEntry> participants = new ArrayList<>();

}
