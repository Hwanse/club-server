package com.study.clubserver.domain.club.clubAccount;

import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubAccountRepository extends JpaRepository<ClubAccount, Long>, ClubAccountCustomRepository {

  Optional<ClubAccount> findClubAccountByClubAndAccount(Club club, Account account);

}
