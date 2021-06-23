package com.study.clubserver.api.controller.club;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountRepository;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

class ClubControllerTest extends BaseControllerTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ClubService clubService;

  @BeforeEach
  @Transactional
  public void setupUser() {
    Account account = new Account("hwanse", "hwanse@email.com",
                                  "hwanse", "hwanse");
    accountService.join(account);
  }

  @AfterEach
  @Transactional
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

    // when & then
    mockMvc.perform(get("/api/clubs")
                    .queryParam("page", "0")
                    .queryParam("size", "10"))
           .andDo(print())
           .andExpect(status().isOk());
  }

}