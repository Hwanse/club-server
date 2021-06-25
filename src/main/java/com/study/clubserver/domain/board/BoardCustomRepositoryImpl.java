package com.study.clubserver.domain.board;

import static com.study.clubserver.domain.account.QAccount.account;
import static com.study.clubserver.domain.board.QBoard.board;
import static com.study.clubserver.domain.club.clubAccount.QClubAccount.clubAccount;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.clubserver.domain.account.QAccount;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.clubAccount.QClubAccount;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

  private final JPAQueryFactory factory;

  @Override
  public Page<Board> findBoardsWithWriter(Club club, Pageable pageable) {
    QueryResults<Board> results = factory
      .select(board)
      .from(board)
      .join(board.writer, clubAccount).fetchJoin()
      .join(clubAccount.account, account).fetchJoin()
      .where(board.club.eq(club))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(board.createdAt.desc())
      .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }
}
