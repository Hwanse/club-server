package com.study.clubserver.api.dto.interest;

import com.study.clubserver.domain.club.clubInterest.ClubInterest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestDto {

  private Long id;

  private String name;

  private Long interestCollectionId;

  public InterestDto(ClubInterest clubInterest) {
    this.id = clubInterest.getInterests().getId();
    this.name = clubInterest.getInterests().getName();
    this.interestCollectionId = clubInterest.getInterests().getInterestCollection().getId();
  }
}
