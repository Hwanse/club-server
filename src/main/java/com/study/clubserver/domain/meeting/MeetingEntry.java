package com.study.clubserver.domain.meeting;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.ClubAccount;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MeetingEntry extends CommonEntity {

  @ManyToOne
  private Meeting meeting;

  @ManyToOne
  private ClubAccount participant;

}
