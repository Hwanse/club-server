package com.study.clubserver.domain.meeting.meetingEntry;

import com.study.clubserver.domain.meeting.Meeting;
import java.util.List;

public interface MeetingEntryCustomRepository {

  List<MeetingEntry> findEntryInMeetingByMeeting(Meeting meeting);

}
