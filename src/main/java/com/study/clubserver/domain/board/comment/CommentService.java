package com.study.clubserver.domain.board.comment;

import com.study.clubserver.api.dto.comment.CommentCreateRequest;
import com.study.clubserver.api.dto.comment.CommentDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.board.Board;
import com.study.clubserver.domain.board.BoardRepository;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubRepository;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.club.clubAccount.ClubAccountRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final BoardRepository boardRepository;
  private final ClubRepository clubRepository;
  private final ClubAccountRepository clubAccountRepository;

  @Transactional
  public Comment addComment(Account account, Long clubId, Long boardId,
    CommentCreateRequest request) {
    Club club = getClub(clubId);
    ClubAccount clubAccount = getClubAccount(club, account);
    Board board = getBoard(boardId);

    if (isChildComment(request)) {
      Comment parent = commentRepository.findById(request.getParentCommentId())
                                        .orElseThrow(() -> new IllegalArgumentException("댓글을 조회할 수 없습니다."));
      return commentRepository.save(new Comment(board, clubAccount, request, parent));
    } else {
      return commentRepository.save(new Comment(board, clubAccount, request));
    }
  }

  @Transactional(readOnly = true)
  public Page<CommentDto> getCommentsPage(Long clubId, Long boardId, Pageable pageable) {
    getClub(clubId);
    getBoard(boardId);

    Page<Comment> commentPage = commentRepository.findCommentsWithWriter(boardId, pageable);
    List<CommentDto> commentDtoList = commentPage.stream().map(CommentDto::new).collect(Collectors.toList());

    return new PageImpl<>(commentDtoList, commentPage.getPageable(), commentPage.getTotalElements());
  }

  private boolean isChildComment(CommentCreateRequest request) {
    return request.getParentCommentId() != null;
  }

  private Club getClub(Long clubId) {
    return clubRepository.findById(clubId)
                         .orElseThrow(() -> new IllegalArgumentException("클럽 정보를 조회할 수 없습니다."));
  }

  private ClubAccount getClubAccount(Club club, Account account) {
    return clubAccountRepository.findClubAccountWithRole(club, account)
                                .orElseThrow(() -> new RuntimeException("클럽 구성원이 아닙니다."));
  }

  private Board getBoard(Long boardId) {
    return boardRepository.findById(boardId)
                          .orElseThrow(() -> new IllegalArgumentException("게시글 정보를 조회할 수 없습니다."));
  }

}
