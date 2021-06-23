package com.study.clubserver.api.controller.club;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
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

class ClubControllerTest extends BaseControllerTest {

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

  private Club setupClub() {
    // given
    ClubCreateRequest request = ClubCreateRequest.builder()
                                                 .title("클럽1")
                                                 .description("설명")
                                                 .limitMemberCount(10)
                                                 .interestList(List.of(1L, 2L))
                                                 .zoneList(List.of(101L, 102L))
                                                 .build();
    Account account = accountRepository.findWithAccountRoleByUserId("hwanse").get();

    return clubService.createClub(account, request);
  }

  @Test
  @DisplayName("클럽 생성 API - success")
  @WithMockJwtAuthentication
  public void createClub() throws Exception {
    // given
    ClubCreateRequest request = ClubCreateRequest.builder()
                                               .title("클럽1")
                                               .description("설명")
                                               .limitMemberCount(10)
                                               .interestList(List.of(2L, 3L))
                                               .zoneList(List.of(101L, 102L))
                                               .build();

    // when & then
    mockMvc.perform(post("/api/clubs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaTypes.HAL_JSON_VALUE))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.id").exists())
           .andExpect(jsonPath("$.data.title").exists())
           .andExpect(jsonPath("$.data.description").exists())
           .andExpect(jsonPath("$.data.memberCount").exists())
           .andExpect(jsonPath("$.data.limitMemberCount").exists())
           .andExpect(jsonPath("$.data.bannerImageUrl").isEmpty())
           .andExpect(jsonPath("$.success").value(true));

  }

  @Test
  @DisplayName("클럽 정보 조회 API - success")
  @WithMockJwtAuthentication
  public void getClub() throws Exception {
    // given
    Club club = setupClub();

    // when & then
    mockMvc.perform(get("/api/clubs/{id}", club.getId()))
           .andDo(print())
           .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").exists())
          .andExpect(jsonPath("$.success").value(true))
          .andExpect(jsonPath("$.data.clubInfo").exists())
          .andExpect(jsonPath("$.data.clubAccountStatus").exists())
          .andExpect(jsonPath("$.data.clubAccountsInfo").exists());
  }

  @Test
  @DisplayName("클럽 리스트 조회 (페이징)")
  @WithMockJwtAuthentication
  public void getClubPage() throws Exception {
    // given
    for (int i = 0; i < 30; i++) {
      setupClub();
    }
    int page = 0;
    int size = 10;

    // when & then
    mockMvc.perform(get("/api/clubs")
                    .queryParam("page", String.valueOf(page))
                    .queryParam("size", String.valueOf(size)))
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

}