package com.study.clubserver.domain.interest;

import static com.study.clubserver.domain.interest.QInterest.interest;
import static com.study.clubserver.domain.interest.QInterestCollection.interestCollection;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InterestCustomRepositoryImpl implements InterestCustomRepository {

  private final JPAQueryFactory factory;

  @Override
  public List<Interest> findInterestsInInterestCollection(Long interestCollectionId) {
    return factory
      .select(interest)
      .from(interest)
      .join(interest.interestCollection, interestCollection).fetchJoin()
      .where(interestCollection.id.eq(interestCollectionId))
      .fetch();
  }

  @Override
  public List<Interest> findInterestsByIdList(List<Long> idList) {
    return factory
      .select(interest)
      .from(interest)
      .where(isIn(idList))
      .fetch();
  }

  private BooleanExpression isIn(List<Long> idList) {
    if (idList == null || idList.isEmpty()) {
      return null;
    }
    return interest.id.in(idList);
  }
}
