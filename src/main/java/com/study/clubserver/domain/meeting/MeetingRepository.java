package com.study.clubserver.domain.meeting;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingCustomRepository {

  @EntityGraph(attributePaths = "meetingLeader", type = EntityGraphType.FETCH)
  Meeting findMeetingWithMeetingLeaderById(Long meetingId);

}
