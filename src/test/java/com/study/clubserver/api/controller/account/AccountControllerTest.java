package com.study.clubserver.api.controller.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.domain.role.RoleType;
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

  @Test
  @DisplayName("회원가입 API - success")
  public void join() throws Exception {
    JoinRequest request = JoinRequest.builder()
                                   .userId("hwanse")
                                   .email("hwanse@email.com")
                                   .name("hwanse")
                                   .password("hwanse")
                                   .build();

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

}