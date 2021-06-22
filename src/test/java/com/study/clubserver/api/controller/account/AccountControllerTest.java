package com.study.clubserver.api.controller.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.api.dto.account.LoginRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountRepository;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class AccountControllerTest extends BaseControllerTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  private String id = "hwanse";
  private String password = "hwanse";
  private String email = "hwanse@email.com";

  @BeforeEach
  public void setup() {
    Account account = new Account(id, email, password, id);
    accountService.join(account);
  }

  @AfterEach
  public void clear() {
    accountRepository.findAll().forEach(
      account -> account.deleteRole()
    );
  }

  @Test
  @DisplayName("회원가입 API - success")
  public void join() throws Exception {
    // given
    String testId = "test";
    String testMail = "test@email.com";
    JoinRequest request = JoinRequest.builder()
                                     .userId(testId)
                                     .email(testMail)
                                     .name(testId)
                                     .password(testId)
                                     .build();

    // when & then
    mockMvc.perform(post("/api/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.userId").exists())
           .andExpect(jsonPath("$.data.email").exists())
           .andExpect(jsonPath("$.data.name").exists())
           .andExpect(jsonPath("$.data.roles").value(RoleType.USER.getRoleName()))
           .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("로그인 API - success")
  public void login() throws Exception {
    // given
    LoginRequest loginRequest = new LoginRequest(id, password);

    // when & then
    mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.token").exists())
           .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("유저 정보 조회 API - success")
  @WithMockJwtAuthentication
  public void profile() throws Exception {
    mockMvc.perform(get("/api/profile"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.userId").exists())
           .andExpect(jsonPath("$.data.email").exists())
           .andExpect(jsonPath("$.data.name").exists())
           .andExpect(jsonPath("$.data.roles").value(RoleType.USER.getRoleName()))
           .andExpect(jsonPath("$.success").value(true));
  }

}