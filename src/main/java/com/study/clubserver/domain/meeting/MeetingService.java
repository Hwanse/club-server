package com.study.clubserver.domain.meeting;

import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.api.dto.meeting.MeetingDetailsDto;
import com.study.clubserver.api.dto.meeting.MeetingDto;
import com.study.clubserver.api.dto.meeting.MeetingEntryDto;
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
    Meeting meeting = meetingRepository.save(new Meeting(club, clubAccount,request));
    // 생성된 모임에 구성원 추가
    meetingEntryRepository.save(new MeetingEntry(meeting, clubAccount));
    // 미팅 인원 증가
    meeting.incrementEntryCount();

    return meeting;
  }

  @Transactional(readOnly = true)
  public Page<MeetingDto> getMeetingsPage(Long clubId, Pageable pageable) {
    Club club = getClub(clubId);
    // 해당 클럽의 모임 리스트를 조회
    Page<Meeting> meetingPage = meetingRepository.findMeetingsOfClubWithPaging(club, pageable);
    List<MeetingDto> meetingDtoList = meetingPage.stream().map(MeetingDto::new).collect(Collectors.toList());

    return new PageImpl<>(meetingDtoList, meetingPage.getPageable(), meetingPage.getTotalElements());
  }

  @Transactional(readOnly = true)
  public MeetingDetailsDto getMeetingDetails(Long clubId, Long meetingId, Account account) {
    Club club = getClub(clubId);
    getClubAccount(club, account);

    Meeting meeting = meetingRepository.findMeetingWithMeetingLeaderById(meetingId);
    List<MeetingEntryDto> participants = meetingEntryRepository
      .findEntryInMeetingByMeeting(meeting).stream()
      .map(MeetingEntryDto::new).collect(Collectors.toList());

    return new MeetingDetailsDto(new MeetingDto(meeting), participants);
  }

  @Transactional
  public void participateMeeting(Account account, Long clubId, Long meetingId) {
    Club club = getClub(clubId);
    ClubAccount clubAccount = getClubAccount(club, account);
    Meeting meeting = meetingRepository.findMeetingWithMeetingLeaderById(meetingId);

    checkParticipateMeeting(clubAccount, meeting);

    // 참여 신청
    meetingEntryRepository.save(new MeetingEntry(meeting, clubAccount));
    meeting.incrementEntryCount();

  }

  private void checkParticipateMeeting(ClubAccount clubAccount, Meeting meeting) {
    if (meeting.isEnd()) {
      throw new RuntimeException("종료된 모임입니다.");
    }

    if (meeting.getEntryCount() >= meeting.getLimitEntryCount()) {
      throw new RuntimeException("인원이 찬 모임입니다.");
    }

    if (isPartipateInMeeting(clubAccount, meeting)) {
      throw new RuntimeException("이미 참가중인 모임입니다,");
    }
  }

  private boolean isPartipateInMeeting(ClubAccount clubAccount, Meeting meeting) {
    return meeting.getParticipants().stream()
                  .anyMatch(meetingEntry -> meetingEntry.getParticipant() == clubAccount);
  }

  private Club getClub(Long clubId) {
    return clubRepository.findById(clubId)
                         .orElseThrow(
                           () -> new IllegalArgumentException("클럽 정보를 조회할 수 없습니다.")
                         );
  }

  private ClubAccount getClubAccount(Club club, Account account) {
    return clubAccountRepository.findClubAccountWithRole(club, account)
                                .orElseThrow(() -> new RuntimeException("클럽 구성원이 아닙니다."));
  }

}
