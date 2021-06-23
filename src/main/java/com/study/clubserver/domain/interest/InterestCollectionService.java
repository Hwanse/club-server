package com.study.clubserver.domain.interest;

import com.study.clubserver.api.dto.interest.InterestCollectionDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestCollectionService {

  private final InterestCollectionRepository interestCollectionRepository;

  public List<InterestCollection> getInterestCollections() {
    return interestCollectionRepository.findAll();
  }

  @Transactional
  public InterestCollection createInterestCollection(InterestCollectionDto interestCollectionDto) {
    return interestCollectionRepository.save(new InterestCollection(interestCollectionDto));
  }

}
