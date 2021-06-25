package com.study.clubserver.domain.meeting.meetingEntry;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.meeting.Meeting;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MeetingEntry extends CommonEntity {

  @ManyToOne
  private Meeting meeting;

  @ManyToOne
  private ClubAccount participant;

  public MeetingEntry(Meeting meeting, ClubAccount participant) {
    this.meeting = meeting;
    meeting.getParticipants().add(this);
    this.participant = participant;
  }
}
