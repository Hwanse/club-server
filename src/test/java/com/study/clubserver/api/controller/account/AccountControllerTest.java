package com.study.clubserver.api.controller.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
  @Order(1)
  public void join() throws Exception {
    // given
    JoinRequest request = JoinRequest.builder()
                                     .userId(userId)
                                     .email(email)
                                     .name(username)
                                     .password(password)
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
  @Order(2)
  public void login() throws Exception {
    // given
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

  @Test
  @DisplayName("유저 정보 조회 API - success")
  @Order(3)
  public void userInfo() throws Exception {
    mockMvc.perform(get("/api/profile"))
           .andDo(print())
           .andExpect(status().isOk());
  }

}