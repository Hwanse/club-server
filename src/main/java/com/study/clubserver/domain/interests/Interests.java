package com.study.clubserver.domain.interests;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Interests extends CommonEntity {

  @Column(nullable = false)
  private String name;

}
