package com.study.clubserver.api.dto.meeting;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.domain.meeting.Meeting;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingDto {

  private Long id;

  private String title;

  private String description;

  private String meetingStartTime;

  private String meetingEndTime;

  private int entryCount;

  private int limitEntryCount;

  private String meetingAddress;

  private Long clubId;

  private ClubAccountInfoDto meetingLeader;

  public MeetingDto(Meeting meeting) {
    this.id = meeting.getId();
    this.title = meeting.getTitle();
    this.description = meeting.getDescription();
    this.meetingStartTime = meeting.getMeetingStartTime()
                                   .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    this.meetingEndTime = meeting.getMeetingEndTime()
                                 .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    this.entryCount = meeting.getEntryCount();
    this.limitEntryCount = meeting.getLimitEntryCount();
    this.meetingAddress = meeting.getMeetingAddress();
    this.clubId = meeting.getClub().getId();
    this.meetingLeader = new ClubAccountInfoDto(meeting.getMeetingLeader());
  }

}
