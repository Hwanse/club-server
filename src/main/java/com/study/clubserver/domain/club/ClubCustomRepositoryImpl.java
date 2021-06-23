package com.study.clubserver.domain.club;

import static com.study.clubserver.domain.club.QClub.club;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ClubCustomRepositoryImpl implements ClubCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Club> findClubsWithPaging(Pageable pageable) {
    QueryResults<Club> queryResults = queryFactory
      .select(club)
      .from(club)
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(club.createdAt.desc())
      .fetchResults();
    return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
  }
}
