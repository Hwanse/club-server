package com.study.clubserver.domain.meeting;

import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.meeting.meetingEntry.MeetingEntry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Meeting extends CommonEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private LocalDateTime meetingStartTime;

  private LocalDateTime meetingEndTime;

  private int entryCount;

  private int limitEntryCount;

  private String meetingAddress;

  private boolean isEnd;

  @ManyToOne
  private ClubAccount meetingLeader;

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "participant")
  private List<MeetingEntry> participants = new ArrayList<>();

  public Meeting(Club club, ClubAccount meetingLeader, MeetingCreateRequest request) {
    this.title = request.getTitle();
    this.description = request.getDescription();
    this.meetingStartTime = request.getMeetingStartTime();
    this.meetingEndTime = request.getMeetingEndTime();
    this.entryCount = 0;
    this.limitEntryCount = request.getLimitEntryCount();
    this.meetingAddress = request.getMeetingAddress();
    this.club = club;
    this.isEnd = false;
    this.meetingLeader = meetingLeader;
  }

  public void incrementEntryCount() {
    this.entryCount++;
  }

}
