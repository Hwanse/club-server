package com.study.clubserver.domain.interest;

import com.study.clubserver.api.dto.interest.InterestDto;
import com.study.clubserver.domain.interest.interestCollection.InterestCollection;
import com.study.clubserver.domain.interest.interestCollection.InterestCollectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {

  private final InterestRepository interestRepository;
  private final InterestCollectionRepository interestCollectionRepository;

  public List<Interest> getInterestsInInterestCollection(Long interestCollectionId) {
    return interestRepository.findInterestsInInterestCollection(interestCollectionId);
  }

  public Interest createInterest(Long interestCollectionId, InterestDto interestDto) {
    InterestCollection interestCollection = interestCollectionRepository
      .findById(interestCollectionId).orElseThrow(IllegalArgumentException::new);
    return interestRepository.save(new Interest(interestCollection, interestDto));
  }

  public Interest findInterestInfo(Long interestId) {
    return interestRepository.findById(interestId).orElseThrow(IllegalArgumentException::new);
  }

}
