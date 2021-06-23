package com.study.clubserver.domain.interest;

import com.study.clubserver.domain.CommonEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class InterestCollection extends CommonEntity {

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "interestCollection")
  private List<Interest> interests;

}
