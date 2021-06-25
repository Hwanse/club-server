package com.study.clubserver.domain.board;

import com.study.clubserver.api.dto.board.BoardCreateRequest;
import com.study.clubserver.domain.CommonEntity;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "club_board")
@NoArgsConstructor
@AllArgsConstructor
public class Board extends CommonEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "club_account_id", nullable = false)
  private ClubAccount writer;

  @ManyToOne
  @JoinColumn(name = "club_id", nullable = false)
  private Club club;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

  public Board(Club club, ClubAccount clubAccount, BoardCreateRequest request) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.writer = clubAccount;
    this.club = club;
    club.getBoards().add(this);
  }
}
