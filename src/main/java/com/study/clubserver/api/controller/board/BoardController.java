package com.study.clubserver.api.controller.board;

import static com.study.clubserver.api.dto.ApiResult.OK;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.study.clubserver.api.dto.ApiResult;
import com.study.clubserver.api.dto.board.BoardCreateRequest;
import com.study.clubserver.api.dto.board.BoardDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.board.BoardService;
import com.study.clubserver.security.CurrentAccount;
import javax.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/boards")
public class BoardController {

  private final BoardService boardService;

  @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity createBoard(@CurrentAccount Account account, @PathVariable Long clubId,
    @Valid @RequestBody BoardCreateRequest request) {
    WebMvcLinkBuilder linkBuilder = linkTo(BoardController.class, clubId).slash("");

    return ResponseEntity.created(linkBuilder.toUri())
                         .body(
                           OK(new BoardDto(boardService.createBoard(clubId, account, request)))
                         );
  }

  @GetMapping("/{boardId}")
  public ApiResult getBoard(@CurrentAccount Account account, @PathVariable Long clubId,
    @PathVariable Long boardId) {
    return OK(
      new BoardDto(boardService.getBoard(clubId, boardId))
    );
  }

  @GetMapping
  public ApiResult queryBoardPage(@CurrentAccount Account account, @PathVariable Long clubId,
    @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
    return OK(
      boardService.queryBoardPage(clubId, pageable)
    );
  }

}
