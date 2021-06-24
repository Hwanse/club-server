package com.study.clubserver.domain.club.clubAccount;

import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import java.util.List;
import java.util.Optional;

public interface ClubAccountCustomRepository {

  Optional<ClubAccount> findClubAccountWithRole(Club club, Account account);

  List<ClubAccount> findClubAccountsOfClub(Club club);

}
