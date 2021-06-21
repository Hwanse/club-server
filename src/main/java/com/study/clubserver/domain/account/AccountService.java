package com.study.clubserver.domain.account;

import com.study.clubserver.domain.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountRoleService accountRoleService;
  private final PasswordEncoder passwordEncoder;

  public Account join(Account account) {
    account.encodePassword(passwordEncoder.encode(account.getPassword()));
    accountRepository.save(account);
    accountRoleService.addRole(account, RoleType.USER);
    return account;
  }

}
