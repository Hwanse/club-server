package com.study.clubserver.domain.interest;

import com.study.clubserver.api.dto.interest.InterestDto;
import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Interest extends CommonEntity {

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "interest_collection_id")
  private InterestCollection interestCollection;

  public Interest(InterestCollection interestCollection, InterestDto interestDto) {
    this.name = interestDto.getName();
    this.interestCollection = interestCollection;
    interestCollection.getInterests().add(this);
  }
}
