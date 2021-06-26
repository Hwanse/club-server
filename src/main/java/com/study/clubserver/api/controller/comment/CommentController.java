package com.study.clubserver.api.controller.comment;

import static com.study.clubserver.api.dto.ApiResult.OK;

import com.study.clubserver.api.dto.ApiResult;
import com.study.clubserver.api.dto.comment.CommentCreateRequest;
import com.study.clubserver.api.dto.comment.CommentDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.board.comment.CommentService;
import com.study.clubserver.security.CurrentAccount;
import javax.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/boards/{boardId}/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public ApiResult addComment(@CurrentAccount Account account, @PathVariable Long clubId,
    @PathVariable Long boardId, @Valid @RequestBody CommentCreateRequest request) {
    return OK(
      new CommentDto(commentService.addComment(account, clubId, boardId, request))
    );
  }

  @GetMapping
  public ApiResult queryCommentPage(@CurrentAccount Account account, @PathVariable Long clubId,
    @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable,
    @PathVariable Long boardId) {
    return OK(
      commentService.getCommentsPage(clubId, boardId, pageable)
    );
  }

}
