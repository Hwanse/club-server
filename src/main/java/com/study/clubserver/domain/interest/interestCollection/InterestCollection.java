package com.study.clubserver.domain.interest.interestCollection;

import com.study.clubserver.api.dto.interest.InterestCollectionDto;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.interest.Interest;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterestCollection extends CommonEntity {

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "interestCollection")
  private List<Interest> interests;

  public InterestCollection(InterestCollectionDto interestCollectionDto) {
    this.name = interestCollectionDto.getName();
  }
}
