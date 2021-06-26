package com.study.clubserver.api.dto.comment;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.domain.board.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

  private Long id;

  private String content;

  private int depth;

  private ClubAccountInfoDto writer;

  private Long parentCommentId;

  public CommentDto(Comment comment) {
    this.id = comment.getId();
    this.content = comment.getContent();
    this.depth = comment.getDepth();
    this.writer = new ClubAccountInfoDto(comment.getWriter());
    this.parentCommentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
  }
}
