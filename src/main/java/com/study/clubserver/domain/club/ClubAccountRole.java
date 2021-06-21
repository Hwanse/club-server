package com.study.clubserver.domain.club;

import com.study.clubserver.domain.role.Role;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class ClubAccountRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "club_role_id")
  private Role clubRole;

  @OneToOne(mappedBy = "clubAccountRole")
  @JoinColumn(name = "club_account_id")
  private ClubAccount clubAccount;

}
