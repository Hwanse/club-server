package com.study.clubserver.api.dto.interest;

import com.study.clubserver.domain.interest.InterestCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestCollectionDto {

  private Long id;

  private String name;

  public InterestCollectionDto(InterestCollection interestCollection) {
    this.id = interestCollection.getId();
    this.name = interestCollection.getName();
  }

}
