package com.study.clubserver.domain.zone;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Zone extends CommonEntity {

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_zone_id")
  private Zone parentZone;

  private String fullName;

  private int level;

}
