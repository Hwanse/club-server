package com.study.clubserver.api.dto.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

  @NotEmpty
  private String userId;

  @Email
  private String email;

  @NotEmpty
  private String name;

  @NotEmpty
  private String password;

}
