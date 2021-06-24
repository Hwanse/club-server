package com.study.clubserver.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingEntryRepository extends JpaRepository<MeetingEntry, Long>, MeetingEntryCustomRepository {

}
