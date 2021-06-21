package com.study.clubserver.api.controller.account;

import static com.study.clubserver.api.common.ApiResult.OK;

import com.study.clubserver.api.common.ApiResult;
import com.study.clubserver.api.dto.account.AccountDto;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/join")
  public ApiResult join(@RequestBody JoinRequest joinRequest) {
    Account account = accountService.join(new Account(joinRequest));
    return OK(new AccountDto(account));
  }

}
