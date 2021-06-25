package com.study.clubserver.domain.board;

import com.study.clubserver.domain.club.Club;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {

  Page<Board> findBoardsWithWriter(Club club, Pageable pageable);

}
