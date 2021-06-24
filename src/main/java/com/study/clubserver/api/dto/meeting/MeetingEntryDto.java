package com.study.clubserver.api.dto.meeting;

import com.study.clubserver.domain.meeting.MeetingEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingEntryDto {

  private Long meetingEntryId;

  private Long accountId;

  private String userId;

  private String userName;

  private String role;

  public MeetingEntryDto(MeetingEntry meetingEntry) {
    this.meetingEntryId = meetingEntry.getId();
    this.accountId = meetingEntry.getParticipant().getAccount().getId();
    this.userId = meetingEntry.getParticipant().getAccount().getUserId();
    this.userName = meetingEntry.getParticipant().getAccount().getName();
    this.role = meetingEntry.getParticipant().getClubAccountRole().getClubRole().getRoleType().getRoleName();
  }

}
