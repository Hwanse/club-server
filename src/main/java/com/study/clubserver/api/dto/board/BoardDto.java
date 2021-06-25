package com.study.clubserver.api.dto.board;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

  private Long id;

  private String title;

  private String content;

  private ClubAccountInfoDto writer;

  private Long clubId;

  public BoardDto(Board board) {
    this.id = board.getId();
    this.title = board.getTitle();
    this.content = board.getContent();
    this.writer = new ClubAccountInfoDto(board.getWriter());
    this.clubId = board.getClub().getId();
  }
}
