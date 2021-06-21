package com.study.clubserver.domain.board;

import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.ClubAccount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.mapping.ToOne;

@Entity
@Table(name = "board_comment")
@Getter
public class Comment extends CommonEntity {

  @Column(nullable = false)
  private String content;

  private int depth;

  @ManyToOne
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @ManyToOne
  @JoinColumn(name = "club_account_id", nullable = false)
  private ClubAccount writer;

  @ManyToOne
  @JoinColumn(name = "parent_comment_id")
  private Comment parentComment;

}
