package com.study.clubserver.api.controller.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.api.dto.account.LoginRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.domain.role.RoleType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private AccountService accountService;

  private String userId = "hwanse";
  private String username = "hwanse";
  private String password = "hwanse";
  private String email = "hwanse@email.com";

  @Test
  @DisplayName("회원가입 API - success")
  public void join() throws Exception {
    // given
    JoinRequest request = JoinRequest.builder()
                                     .userId("test")
                                     .email("test@email.com")
                                     .name("test")
                                     .password("test")
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
           .andExpect(jsonPath("$.data.roles").value(RoleType.USER.name()))
           .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("로그인 API - success")
  public void login() throws Exception {
    // given
    Account account = new Account(userId, email, username, password);
    accountService.join(account);
    LoginRequest loginRequest = new LoginRequest(userId, password);

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

}