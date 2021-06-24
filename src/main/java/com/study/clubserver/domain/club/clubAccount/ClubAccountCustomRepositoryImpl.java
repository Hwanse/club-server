package com.study.clubserver.domain.club.clubAccount;

import static com.study.clubserver.domain.account.QAccount.account;
import static com.study.clubserver.domain.account.QAccountRole.accountRole;
import static com.study.clubserver.domain.club.QClub.club;
import static com.study.clubserver.domain.club.clubAccount.QClubAccount.clubAccount;
import static com.study.clubserver.domain.club.clubAccountRole.QClubAccountRole.clubAccountRole;
import static com.study.clubserver.domain.role.QRole.role;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.account.QAccountRole;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.role.QRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClubAccountCustomRepositoryImpl implements ClubAccountCustomRepository {

  private final JPAQueryFactory factory;

  @Override
  public Optional<ClubAccount> findClubAccountWithRole(Club paramClub, Account paramAccount) {
    return Optional.ofNullable(
      factory
        .select(clubAccount)
        .from(clubAccount)
        .join(clubAccount.club, club).fetchJoin()
        .join(clubAccount.account, account).fetchJoin()
        .join(account.accountRole, accountRole).fetchJoin()
        .join(clubAccount.clubAccountRole, clubAccountRole).fetchJoin()
        .join(clubAccountRole.clubRole, role).fetchJoin()
        .where(
          clubAccount.club.eq(paramClub),
          clubAccount.account.eq(paramAccount)
        )
        .fetchOne()
    );
  }

  @Override
  public List<ClubAccount> findClubAccountsOfClub(Club paramClub) {
    return factory
      .select(clubAccount)
      .from(clubAccount)
      .join(clubAccount.account, account).fetchJoin()
      .join(clubAccount.clubAccountRole, clubAccountRole).fetchJoin()
      .join(clubAccountRole.clubRole, role)
      .where(clubAccount.club.eq(paramClub))
      .fetch();
  }


}
