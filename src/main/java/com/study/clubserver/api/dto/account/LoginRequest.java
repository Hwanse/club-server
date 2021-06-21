package com.study.clubserver.api.dto.account;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

  @NotEmpty
  private String userId;

  @NotEmpty
  private String password;

}
