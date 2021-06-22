package com.study.clubserver.api.controller.club;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountRepository;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

class ClubControllerTest extends BaseControllerTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

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

}