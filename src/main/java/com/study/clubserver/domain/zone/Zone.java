package com.study.clubserver.domain.zone;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Zone extends CommonEntity {

  @Column(nullable = false)
  private String name;

  private String parentZoneName;

  private int level;

}
