package com.study.clubserver.domain.interest;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long>, InterestCustomRepository {

}
