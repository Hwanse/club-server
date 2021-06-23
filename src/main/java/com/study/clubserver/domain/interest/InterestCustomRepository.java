package com.study.clubserver.domain.interest;

import java.util.List;

public interface InterestCustomRepository {

  List<Interest> findInterestsInInterestCollection(Long interestCollectionId);

}
