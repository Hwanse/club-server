package com.study.clubserver.domain.meeting;

import static com.study.clubserver.domain.club.QClub.club;
import static com.study.clubserver.domain.club.clubAccount.QClubAccount.clubAccount;
import static com.study.clubserver.domain.meeting.QMeeting.meeting;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.QClub;
import com.study.clubserver.domain.club.clubAccount.QClubAccount;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MeetingCustomRepositoryImpl implements MeetingCustomRepository {

  private final JPAQueryFactory factory;

  @Override
  public Page<Meeting> findMeetingsOfClubWithPaging(Club paramClub, Pageable pageable) {
    QueryResults<Meeting> results = factory
      .select(meeting)
      .from(meeting)
      .join(meeting.club, club).fetchJoin()
      .join(meeting.meetingLeader, clubAccount).fetchJoin()
      .where(meeting.club.eq(paramClub))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(meeting.createdAt.desc())
      .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }

}
