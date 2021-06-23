package com.study.clubserver.domain.club;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubCustomRepository {

  Page<Club> findClubsWithPaging(Pageable pageable);

}
