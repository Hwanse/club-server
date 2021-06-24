package com.study.clubserver.domain.meeting;

import static com.study.clubserver.domain.club.clubAccount.QClubAccount.clubAccount;
import static com.study.clubserver.domain.club.clubAccountRole.QClubAccountRole.clubAccountRole;
import static com.study.clubserver.domain.meeting.QMeeting.meeting;
import static com.study.clubserver.domain.meeting.QMeetingEntry.meetingEntry;
import static com.study.clubserver.domain.role.QRole.role;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetingEntryCustomRepositoryImpl implements MeetingEntryCustomRepository{

  private final JPAQueryFactory factory;

  @Override
  public List<MeetingEntry> findEntryInMeetingByMeeting(Meeting paramMeeting) {
    return factory
      .select(meetingEntry)
      .from(meetingEntry)
      .join(meetingEntry.meeting, meeting).fetchJoin()
      .join(meetingEntry.participant, clubAccount).fetchJoin()
      .join(clubAccount.clubAccountRole, clubAccountRole).fetchJoin()
      .join(clubAccountRole.clubRole, role).fetchJoin()
      .fetch();
  }
}
