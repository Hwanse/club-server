package com.study.clubserver.domain.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsAccountByUserId(String userId);

  Optional<Account> findByUserId(String userId);
}
