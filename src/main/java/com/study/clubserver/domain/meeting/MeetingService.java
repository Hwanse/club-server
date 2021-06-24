package com.study.clubserver.domain.meeting;

import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubRepository;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.club.clubAccount.ClubAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final MeetingEntryRepository meetingEntryRepository;

  private final ClubRepository clubRepository;
  private final ClubAccountRepository clubAccountRepository;

  @Transactional
  public Meeting createMeeting(Account account, Long clubId, MeetingCreateRequest request) {
    Club club = clubRepository.findById(clubId)
                              .orElseThrow(
                                () -> new IllegalArgumentException("클럽 정보를 조회할 수 없습니다.")
                              );
    // 클럽 구성원인 현재 유저 조회
    ClubAccount clubAccount = getClubAccount(club, account);
    // 모임 생성
    Meeting meeting = meetingRepository.save(new Meeting(club, request));
    // 생성된 모임에 구성원 추가
    meetingEntryRepository.save(new MeetingEntry(meeting, clubAccount));

    meeting.incrementEntryCount();

    return meeting;
  }

  private ClubAccount getClubAccount(Club club, Account account) {
    return clubAccountRepository.findClubAccountByClubAndAccount(club, account)
                                .orElseThrow(() -> new RuntimeException("클럽 구성원이 아닙니다."));
  }

}
