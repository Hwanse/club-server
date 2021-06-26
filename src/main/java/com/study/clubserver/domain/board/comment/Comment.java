package com.study.clubserver.domain.board.comment;

import com.study.clubserver.api.dto.comment.CommentCreateRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.board.Board;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "board_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

  public Comment(Board board, ClubAccount writer, CommentCreateRequest request) {
    this.content = request.getContent();
    this.depth = 1;
    this.board = board;
    board.getComments().add(this);
    this.writer = writer;
  }

  public Comment(Board board, ClubAccount writer, CommentCreateRequest request, Comment parent) {
    this.content = request.getContent();
    this.depth = parent.getDepth() + 1;
    this.board = board;
    board.getComments().add(this);
    this.writer = writer;
    this.parentComment = parent;
  }

}
