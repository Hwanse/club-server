package com.study.clubserver.domain.interest;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Interest extends CommonEntity {

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "interest_collection_id")
  private InterestCollection interestCollection;

}
