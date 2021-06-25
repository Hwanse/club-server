package com.study.clubserver.domain.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  @EntityGraph(attributePaths = {"writer", "club"}, type = EntityGraphType.FETCH)
  Optional<Board> findBoardWithWriterAndClubById(Long boardId);

}
