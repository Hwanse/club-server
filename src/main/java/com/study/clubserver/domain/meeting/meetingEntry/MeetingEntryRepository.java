package com.study.clubserver.domain.meeting.meetingEntry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingEntryRepository extends JpaRepository<MeetingEntry, Long>, MeetingEntryCustomRepository {

}
