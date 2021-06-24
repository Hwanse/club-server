package com.study.clubserver.domain.meeting;

import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.api.dto.meeting.MeetingDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubRepository;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.club.clubAccount.ClubAccountRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    Club club = getClub(clubId);
    // 클럽 구성원인 현재 유저 조회
    ClubAccount clubAccount = getClubAccount(club, account);
    // 모임 생성
    Meeting meeting = meetingRepository.save(new Meeting(club, request));
    // 생성된 모임에 구성원 추가
    meetingEntryRepository.save(new MeetingEntry(meeting, clubAccount));

    meeting.incrementEntryCount();

    return meeting;
  }

  @Transactional(readOnly = true)
  public Page<MeetingDto> getMeetingsPage(Long clubId, Pageable pageable) {
    Club club = getClub(clubId);

    // 해당 클럽의 모임 리스트를 조회
    Page<Meeting> meetingPage = meetingRepository.findMeetingsOfClubWithPaging(club, pageable);
    List<MeetingDto> meetingDtoList = meetingPage.stream()
                                                 .map(meeting -> new MeetingDto(meeting))
                                                 .collect(Collectors.toList());
    return new PageImpl<>(meetingDtoList, meetingPage.getPageable(), meetingPage.getTotalElements());
  }

  private Club getClub(Long clubId) {
    return clubRepository.findById(clubId)
                         .orElseThrow(
                           () -> new IllegalArgumentException("클럽 정보를 조회할 수 없습니다.")
                         );
  }

  private ClubAccount getClubAccount(Club club, Account account) {
    return clubAccountRepository.findClubAccountByClubAndAccount(club, account)
                                .orElseThrow(() -> new RuntimeException("클럽 구성원이 아닙니다."));
  }

}
