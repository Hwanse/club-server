package com.study.clubserver.domain.meeting;

import com.study.clubserver.domain.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingCustomRepository {

  Page<Meeting> findMeetingsOfClubWithPaging(Club club, Pageable pageable);

}
