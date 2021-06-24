package com.study.clubserver.api.controller.meeting;

import static org.junit.jupiter.api.Assertions.*;
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
           .andExpect(jsonPath("$.data.meetingId").exists())
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

}