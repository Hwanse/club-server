package com.study.clubserver.domain.board.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

  Page<Comment> findCommentsWithWriter(Long boardId, Pageable pageable);

}
