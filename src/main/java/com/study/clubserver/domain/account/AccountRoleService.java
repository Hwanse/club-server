package com.study.clubserver.domain.account;

import com.study.clubserver.domain.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRoleService {

  private final AccountRoleRepository accountRoleRepository;

  public void addRole(Account account, RoleType roleType) {
    accountRoleRepository.save(new AccountRole(account, roleType.getRoleName()));
  }

}
