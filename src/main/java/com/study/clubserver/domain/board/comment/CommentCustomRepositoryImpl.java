package com.study.clubserver.domain.board.comment;

import static com.study.clubserver.domain.account.QAccount.account;
import static com.study.clubserver.domain.board.comment.QComment.comment;
import static com.study.clubserver.domain.club.clubAccount.QClubAccount.clubAccount;
import static com.study.clubserver.domain.club.clubAccountRole.QClubAccountRole.clubAccountRole;
import static com.study.clubserver.domain.role.QRole.role;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.clubserver.domain.account.QAccount;
import com.study.clubserver.domain.club.clubAccount.QClubAccount;
import com.study.clubserver.domain.club.clubAccountRole.QClubAccountRole;
import com.study.clubserver.domain.role.QRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

  private final JPAQueryFactory factory;

  @Override
  public Page<Comment> findCommentsWithWriter(Long boardId, Pageable pageable) {
    QueryResults<Comment> results = factory
      .select(comment)
      .from(comment)
      .join(comment.writer, clubAccount).fetchJoin()
      .join(clubAccount.account, account).fetchJoin()
      .join(clubAccount.clubAccountRole, clubAccountRole).fetchJoin()
      .join(clubAccountRole.clubRole, role).fetchJoin()
      .where(
        comment.board.id.eq(boardId)
        .and(comment.depth.eq(1))
      )
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(comment.createdAt.desc())
      .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }

}
