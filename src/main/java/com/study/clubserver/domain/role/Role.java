package com.study.clubserver.domain.role;

import com.study.clubserver.domain.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
public class Role extends CommonEntity {

  @Enumerated(EnumType.STRING)
  private RoleType roleType;

}
