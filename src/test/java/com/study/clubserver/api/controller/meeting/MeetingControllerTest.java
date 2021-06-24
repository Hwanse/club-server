package com.study.clubserver.api.controller.meeting;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.domain.meeting.Meeting;
import com.study.clubserver.domain.meeting.MeetingService;
import com.study.clubserver.security.WithMockJwtAuthentication;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

class MeetingControllerTest extends BaseControllerTest {

  @Autowired
  ClubService clubService;

  @Autowired
  MeetingService meetingService;

  @BeforeEach
  public void setupUser() {
    Account account = new Account("hwanse", "hwanse@email.com",
                                  "hwanse", "hwanse");
    accountService.join(account);
  }

  @AfterEach
  public void clear() {
    accountRepository.findAll().forEach(
      account -> account.deleteRole()
    );
    accountRepository.deleteAll();
  }

  private Account createUser(String username) {
    Account account = new Account(username, username + "@email.com", username, username);
    return accountService.join(account);
  }

  private Meeting setupMeeting(Club club, String userId) {
    Account account = accountRepository.findWithAccountRoleByUserId(userId).get();
    return meetingService.createMeeting(account, club.getId(), createRequest());
  }

  private MeetingCreateRequest createRequest() {
    LocalDateTime now = LocalDateTime.now();
    return MeetingCreateRequest
      .builder()
      .title("모임 1")
      .description("내용")
      .meetingStartTime(now)
      .meetingEndTime(LocalDateTime.now().plusHours(2))
      .limitEntryCount(10)
      .meetingAddress("서울 강남 가로수길")
      .build();
  }

  private Club setupClub(String userId) {
    ClubCreateRequest request = ClubCreateRequest.builder()
                                                 .title("클럽1")
                                                 .description("설명")
                                                 .limitMemberCount(10)
                                                 .interestList(List.of(1L, 2L))
                                                 .zoneList(List.of(101L, 102L))
                                                 .build();

    Account account = accountRepository.findWithAccountRoleByUserId(userId).get();
    return clubService.createClub(account, request);
  }

  private void clubJoin(String userId, Club club) {
    Account account = accountRepository.findWithAccountRoleByUserId(userId).get();
    clubService.accountJoinToClub(account, club.getId());
  }

  @Test
  @DisplayName("모임 생성 API")
  @WithMockJwtAuthentication
  public void createMeeting() throws Exception {
    // given
    Club club = setupClub("hwanse");
    MeetingCreateRequest request = createRequest();

    // when & then
    mockMvc.perform(post("/api/clubs/{clubId}/meetings", club.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaTypes.HAL_JSON_VALUE))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.data.id").exists())
           .andExpect(jsonPath("$.data.title").exists())
           .andExpect(jsonPath("$.data.description").exists())
           .andExpect(jsonPath("$.data.meetingStartTime").exists())
           .andExpect(jsonPath("$.data.meetingEndTime").exists())
           .andExpect(jsonPath("$.data.limitEntryCount").exists())
           .andExpect(jsonPath("$.data.entryCount").exists())
           .andExpect(jsonPath("$.data.meetingAddress").exists())
           .andExpect(jsonPath("$.data.clubId").exists())
    ;
  }

  @Test
  @DisplayName("모임 리스트 조회 API (페이징)")
  @WithMockJwtAuthentication
  public void queryMeetingPage() throws Exception {
    // given
    Club club = setupClub("hwanse");
    for (int i = 0; i < 30; i++) {
      setupMeeting(club, "hwanse");
    }

    int page = 0;
    int size = 10;

    mockMvc.perform(get("/api/clubs/{clubId}/meetings", club.getId())
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data.content").exists())
           .andExpect(jsonPath("$.data.totalPages").exists())
           .andExpect(jsonPath("$.data.totalElements").exists())
           .andExpect(jsonPath("$.data.size").value(size))
           .andExpect(jsonPath("$.data.numberOfElements").value(size))
           .andExpect(jsonPath("$.data.number").value(page))
           .andExpect(jsonPath("$.data.first").exists())
           .andExpect(jsonPath("$.data.last").exists());
  }

  @Test
  @DisplayName("모임 조회 API")
  @WithMockJwtAuthentication
  public void getMeetingDetails() throws Exception {
    // given
    Club club = setupClub("hwanse");
    Meeting meeting = setupMeeting(club, "hwanse");

    // when & then
    mockMvc.perform(get("/api/clubs/{clubId}/meetings/{meetingId}", club.getId(), meeting.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data.meeting.id").exists())
           .andExpect(jsonPath("$.data.meeting.title").exists())
           .andExpect(jsonPath("$.data.meeting.description").exists())
           .andExpect(jsonPath("$.data.meeting.meetingStartTime").exists())
           .andExpect(jsonPath("$.data.meeting.meetingEndTime").exists())
           .andExpect(jsonPath("$.data.meeting.entryCount").exists())
           .andExpect(jsonPath("$.data.meeting.limitEntryCount").exists())
           .andExpect(jsonPath("$.data.meeting.meetingAddress").exists())
           .andExpect(jsonPath("$.data.meeting.meetingLeader").exists())
           .andExpect(jsonPath("$.data.meeting.clubId").exists())
           .andExpect(jsonPath("$.data.participants[0].meetingEntryId").exists())
           .andExpect(jsonPath("$.data.participants[0].accountId").exists())
           .andExpect(jsonPath("$.data.participants[0].userId").exists())
           .andExpect(jsonPath("$.data.participants[0].userName").exists())
           .andExpect(jsonPath("$.data.participants[0].role").exists());
  }

  @Test
  @DisplayName("모임 참가 API")
  @WithMockJwtAuthentication
  public void enter() throws Exception {
    // given
    Account account = createUser("test");
    Club club = setupClub(account.getUserId());
    Meeting meeting = setupMeeting(club, account.getUserId());
    clubJoin("hwanse", club);

    // when & then
    mockMvc.perform(post("/api/clubs/{clubId}/meetings/{meetingId}/participate",
                         club.getId(), meeting.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").value(true))
           .andExpect(jsonPath("$.success").value(true));
  }

}