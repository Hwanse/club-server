package com.study.clubserver.domain.club.clubAccount;

import com.study.clubserver.api.dto.club.ClubAccountStatusDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubAccountService {

  private final ClubAccountRepository clubAccountRepository;

  public ClubAccountStatusDto getClubAccountDetailWithRole(Club club, Account account) {
    return clubAccountRepository.findClubAccountWithRole(club, account)
                                .map(ClubAccountStatusDto::new)
                                .get();
  }

}
