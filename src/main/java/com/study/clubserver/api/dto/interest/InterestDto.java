package com.study.clubserver.api.dto.interest;

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

  private InterestCollectionDto interestCollection;

}
