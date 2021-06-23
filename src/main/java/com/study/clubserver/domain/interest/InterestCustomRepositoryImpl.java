package com.study.clubserver.domain.interest;

import static com.study.clubserver.domain.interest.QInterest.interest;
import static com.study.clubserver.domain.interest.QInterestCollection.interestCollection;

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
}
