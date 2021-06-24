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
import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

class AccountControllerTest extends BaseControllerTest {

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
  @DisplayName("회원가입 API")
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
    mockMvc.perform(post("/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaTypes.HAL_JSON_VALUE))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.userId").exists())
           .andExpect(jsonPath("$.data.email").exists())
           .andExpect(jsonPath("$.data.name").exists())
           .andExpect(jsonPath("$.data.roles").value(RoleType.USER.getRoleName()))
           .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("로그인 API")
  public void login() throws Exception {
    // given
    LoginRequest loginRequest = new LoginRequest(id, password);

    // when & then
    mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").exists())
           .andExpect(jsonPath("$.data.token").exists())
           .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("유저 정보 조회 API")
  @WithMockJwtAuthentication
  public void profile() throws Exception {
    mockMvc.perform(get("/profile"))
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