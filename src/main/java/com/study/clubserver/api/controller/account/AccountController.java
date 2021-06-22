package com.study.clubserver.api.controller.account;

import static com.study.clubserver.api.dto.ApiResult.OK;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.study.clubserver.api.dto.ApiResult;
import com.study.clubserver.api.dto.account.AccountDto;
import com.study.clubserver.api.dto.account.JoinRequest;
import com.study.clubserver.api.dto.account.LoginRequest;
import com.study.clubserver.api.dto.account.TokenDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.AccountService;
import com.study.clubserver.security.CurrentAccount;
import com.study.clubserver.security.Jwt;
import com.study.clubserver.security.JwtAuthenticationToken;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;
  private final AuthenticationManager authenticationManager;

  @PostMapping(value = "/join", produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity join(@RequestBody JoinRequest joinRequest) {
    Account account = accountService.join(new Account(joinRequest));
    WebMvcLinkBuilder linkBuilder = linkTo(methodOn(AccountController.class).profile(account));
    return ResponseEntity.created(linkBuilder.toUri())
                         .body(
                           OK(new AccountDto(account))
                         );

  }

  @PostMapping("/login")
  public ApiResult login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authenticationToken = new JwtAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());
    String jwt = accountService.login(authenticationToken);
    return OK(new TokenDto(String.format("%s %s", Jwt.BEARER, jwt)));
  }

  @GetMapping("/profile")
  public ApiResult profile(@CurrentAccount Account account) {
    return OK(
      new AccountDto(accountService.getUserProfile(account))
    );
  }

}
