package com.study.clubserver.api.controller.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.board.BoardCreateRequest;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.security.WithMockJwtAuthentication;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

class BoardControllerTest extends BaseControllerTest {

  @Autowired
  ClubService clubService;

  @BeforeEach
  public void setupUser() {
    Account account = new Account("hwanse", "hwanse@email.com",
                                  "hwanse", "hwanse");
    accountService.join(account);
    createUser("test");
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

  private BoardCreateRequest createRequest() {
    return BoardCreateRequest
      .builder()
      .title("제목 1")
      .content("내용 1")
      .build();
  }

  @Test
  @DisplayName("게시글 생성 API")
  @WithMockJwtAuthentication
  public void createBoard() throws Exception {
    // given
    Club club = setupClub("hwanse");
    BoardCreateRequest request = createRequest();

    // when & then
    mockMvc.perform(post("/api/clubs/{clubId}/boards", club.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaTypes.HAL_JSON_VALUE))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.data.id").exists())
           .andExpect(jsonPath("$.data.title").exists())
           .andExpect(jsonPath("$.data.content").exists())
           .andExpect(jsonPath("$.data.clubId").exists());
  }

}