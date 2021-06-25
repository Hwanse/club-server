package com.study.clubserver.domain.board;

import com.study.clubserver.api.dto.board.BoardCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubRepository;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.club.clubAccount.ClubAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final ClubRepository clubRepository;
  private final ClubAccountRepository clubAccountRepository;

  @Transactional
  public Board createBoard(Long clubId, Account account, BoardCreateRequest request) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    ClubAccount clubAccount = getClubAccount(club, account);

    Board board = boardRepository.save(new Board(club, clubAccount, request));
    return board;
  }

  private Club getClub(Long clubId) {
    return clubRepository.findById(clubId)
                         .orElseThrow(() -> new IllegalArgumentException("클럽 정보를 조회할 수 없습니다."));
  }

  private ClubAccount getClubAccount(Club club, Account account) {
    return clubAccountRepository.findClubAccountWithRole(club, account)
                                .orElseThrow(() -> new RuntimeException("클럽 구성원이 아닙니다."));
  }

}
