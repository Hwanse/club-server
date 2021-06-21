package com.study.clubserver.api.controller.account;

import static com.study.clubserver.api.common.ApiResult.OK;

import com.study.clubserver.api.common.ApiResult;
import com.study.clubserver.api.dto.account.AccountDto;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.api.dto.account.LoginRequest;
import com.study.clubserver.api.dto.account.TokenDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.security.Jwt;
import com.study.clubserver.security.JwtAuthenticationToken;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

  private final AccountService accountService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/join")
  public ApiResult join(@RequestBody JoinRequest joinRequest) {
    Account account = accountService.join(new Account(joinRequest));
    return OK(new AccountDto(account));
  }

  @PostMapping("/login")
  public ApiResult login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authenticationToken = new JwtAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());
    Authentication authenticated = authenticationManager.authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authenticated);
    String jwt = (String) authenticated.getDetails();

    return OK(new TokenDto(String.format("%s %s", Jwt.BEARER, jwt)));
  }

}
