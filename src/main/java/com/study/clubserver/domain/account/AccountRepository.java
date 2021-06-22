package com.study.clubserver.domain.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsAccountByUserId(String userId);

  @EntityGraph(attributePaths = "accountRole", type = EntityGraphType.FETCH)
  Optional<Account> findWithAccountRoleByUserId(String userId);
}
