package com.study.clubserver.security;

import com.study.clubserver.domain.account.Account;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserContext extends User {

  private final Account account;

  public UserContext(Account account, Collection<? extends GrantedAuthority> authorities) {
    super(account.getUserId(), account.getPassword(), authorities);
    this.account = account;
  }


}
