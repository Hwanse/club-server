package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.role.Role;
import com.study.clubserver.domain.role.RoleRepository;
import com.study.clubserver.domain.role.RoleService;
import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.security.JwtAuthentication;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

  private final ClubRepository clubRepository;
  private final ClubAccountRepository clubAccountRepository;
  private final ClubAccontRoleRepository clubAccontRoleRepository;
  private final RoleRepository roleRepository;

  @Transactional
  public Club createClub(Account account, ClubCreateRequest request) {
    Club club = new Club(request);
    clubRepository.save(club);

    // clubAccount
    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(club, account));
    // clubAccountRole
    roleRepository.findByRoleType(RoleType.MANAGER)
                  .ifPresent(
                    role -> clubAccontRoleRepository.save(new ClubAccountRole(role, clubAccount))
                  );

    club.incrementMemberCount();

    return club;
  }

}
