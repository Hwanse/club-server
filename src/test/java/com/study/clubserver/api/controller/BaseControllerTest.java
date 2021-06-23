package com.study.clubserver.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.clubserver.domain.account.AccountRepository;
import com.study.clubserver.domain.account.AccountService;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Disabled
@ActiveProfiles("test")
public class BaseControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected AccountService accountService;

  @Autowired
  protected AccountRepository accountRepository;


}
