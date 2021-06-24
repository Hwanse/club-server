package com.study.clubserver.domain.meeting;

import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.Club;
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

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "participant")
  private List<MeetingEntry> participants = new ArrayList<>();

  public Meeting(Club club, MeetingCreateRequest request) {
    this.title = request.getTitle();
    this.description = request.getDescription();
    this.meetingStartTime = request.getMeetingStartTime();
    this.meetingEndTime = request.getMeetingEndTime();
    this.entryCount = 0;
    this.limitEntryCount = request.getLimitEntryCount();
    this.meetingAddress = request.getMeetingAddress();
    this.club = club;
  }

  public void incrementEntryCount() {
    this.entryCount++;
  }

}
