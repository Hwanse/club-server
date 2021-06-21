package com.study.clubserver.api.dto.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinRequest {

  @NotEmpty
  private String userId;

  @Email
  private String email;

  @NotEmpty
  private String name;

  private String password;

}
