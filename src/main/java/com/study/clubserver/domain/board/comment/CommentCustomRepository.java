package com.study.clubserver.domain.board.comment;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

  Page<Comment> findCommentsWithWriter(Long boardId, Pageable pageable);

  List<Comment> findChildCommentsWithWriter(Long boardId, Long parentId);

}
