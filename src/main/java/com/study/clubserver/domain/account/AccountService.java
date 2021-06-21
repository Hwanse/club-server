package com.study.clubserver.domain.account;

import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.security.JwtAuthentication;
import com.study.clubserver.security.UserContext;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

  private final AccountRepository accountRepository;
  private final AccountRoleService accountRoleService;
  private final PasswordEncoder passwordEncoder;

  public Account join(Account account) {
    account.encodePassword(passwordEncoder.encode(account.getPassword()));
    accountRepository.save(account);
    accountRoleService.addRole(account, RoleType.USER);
    return account;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return accountRepository.findByUserId(userId)
                            .map(account -> createUserContext(account))
                            .orElseThrow(() -> new UsernameNotFoundException(userId + " 해당 계정을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public Account getUserProfile(JwtAuthentication authentication) {
    Account account = accountRepository.findByUserId(authentication.getUserId()).get();
    return account;
  }

  private UserContext createUserContext(Account account) {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(account.getAccountRole().getRoleName());
    Set<SimpleGrantedAuthority> grantedAuthorities = Set.of(authority);
    return new UserContext(account, grantedAuthorities);
  }

}
