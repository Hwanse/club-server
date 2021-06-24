package com.study.clubserver.domain.meeting;

import java.util.List;

public interface MeetingEntryCustomRepository {

  List<MeetingEntry> findEntryInMeetingByMeeting(Meeting meeting);

}
