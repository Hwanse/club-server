package com.study.clubserver.api.controller.comment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.clubserver.api.controller.BaseControllerTest;
import com.study.clubserver.api.dto.board.BoardCreateRequest;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.comment.CommentCreateRequest;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.board.Board;
import com.study.clubserver.domain.board.BoardService;
import com.study.clubserver.domain.board.comment.Comment;
import com.study.clubserver.domain.board.comment.CommentService;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.security.WithMockJwtAuthentication;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class CommentControllerTest extends BaseControllerTest {

  @Autowired
  ClubService clubService;

  @Autowired
  BoardService boardService;

  @Autowired
  CommentService commentService;

  @BeforeEach
  public void setupUser() {
    Account account = new Account("hwanse", "hwanse@email.com",
                                  "hwanse", "hwanse");
    accountService.join(account);
  }

  @AfterEach
  public void clear() {
    accountRepository.findAll().forEach(
      account -> account.deleteRole()
    );
    accountRepository.deleteAll();
  }

  private Account createUser(String username) {
    Account account = new Account(username, username + "@email.com", username, username);
    return accountService.join(account);
  }

  private Club setupClub(String userId) {
    ClubCreateRequest request = ClubCreateRequest.builder()
                                                 .title("클럽1")
                                                 .description("설명")
                                                 .limitMemberCount(10)
                                                 .interestList(List.of(1L, 2L))
                                                 .zoneList(List.of(101L, 102L))
                                                 .build();
    Account account = accountRepository.findWithAccountRoleByUserId(userId).get();
    return clubService.createClub(account, request);
  }

  private Board createBoard(Long clubId, String userId, BoardCreateRequest request) {
    Account account = accountRepository.findWithAccountRoleByUserId(userId).get();
    return boardService.createBoard(clubId, account, request);
  }

  private BoardCreateRequest boardCreateRequest() {
    return BoardCreateRequest
      .builder()
      .title("제목")
      .content("내용")
      .build();
  }

  private CommentCreateRequest commentCreateRequest(int number) {
    return CommentCreateRequest
      .builder()
      .content("내용 " + number)
      .build();
  }

  private Comment createComment(Account writer, Club club, Board board,
    CommentCreateRequest request) {
    return commentService.addComment(writer, club.getId(), board.getId(), request);
  }

  @Test
  @DisplayName("댓글 작성 API")
  @WithMockJwtAuthentication
  public void addComment() throws Exception {
    // given
    Club club = setupClub("hwanse");

    Account testUser = createUser("test");
    clubService.accountJoinToClub(testUser, club.getId());

    BoardCreateRequest boardCreateRequest = boardCreateRequest();
    Board board = createBoard(club.getId(), testUser.getUserId(), boardCreateRequest);

    CommentCreateRequest request = commentCreateRequest(1);

    // when & then
    mockMvc.perform(post("/api/clubs/{clubId}/boards/{boardId}/comments", club.getId(), board.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data.id").exists())
           .andExpect(jsonPath("$.data.content").exists())
           .andExpect(jsonPath("$.data.depth").exists())
           .andExpect(jsonPath("$.data.writer.clubAccountId").exists())
           .andExpect(jsonPath("$.data.writer.accountId").exists())
           .andExpect(jsonPath("$.data.writer.userId").exists())
           .andExpect(jsonPath("$.data.writer.userName").exists())
           .andExpect(jsonPath("$.data.writer.role").exists())
           .andExpect(jsonPath("$.data.parentCommentId").isEmpty());
  }


  @Test
  @DisplayName("댓글 리스트 조회 API")
  @WithMockJwtAuthentication
  public void queryCommentPage() throws Exception {
    // given
    Club club = setupClub("hwanse");

    Account testUser = createUser("test");
    clubService.accountJoinToClub(testUser, club.getId());

    BoardCreateRequest boardCreateRequest = boardCreateRequest();
    Board board = createBoard(club.getId(), testUser.getUserId(), boardCreateRequest);

    for (int i = 1; i <= 30; i++) {
      CommentCreateRequest request = commentCreateRequest(i);

      Comment comment = createComment(testUser, club, board, request);
    }

    int size = 10;
    int page = 0;

    // when & then
    mockMvc.perform(get("/api/clubs/{clubId}/boards/{boardId}/comments", club.getId(), board.getId())
                    .queryParam("size", String.valueOf(size))
                    .queryParam("page", String.valueOf(page)))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data.content[0].id").exists())
           .andExpect(jsonPath("$.data.content[0].content").exists())
           .andExpect(jsonPath("$.data.content[0].depth").exists())
           .andExpect(jsonPath("$.data.content[0].writer.clubAccountId").exists())
           .andExpect(jsonPath("$.data.content[0].writer.accountId").exists())
           .andExpect(jsonPath("$.data.content[0].writer.userId").exists())
           .andExpect(jsonPath("$.data.content[0].writer.userName").exists())
           .andExpect(jsonPath("$.data.content[0].writer.role").exists())
           .andExpect(jsonPath("$.data.size").value(size))
           .andExpect(jsonPath("$.data.number").value(page))
           .andExpect(jsonPath("$.data.numberOfElements").exists());
  }

  @Test
  @DisplayName("부모 댓글의 대댓글 리스트 조회 API")
  @WithMockJwtAuthentication
  public void queryChildComments() throws Exception {
    // given
    Club club = setupClub("hwanse");

    Account testUser = createUser("test");
    clubService.accountJoinToClub(testUser, club.getId());

    BoardCreateRequest boardCreateRequest = boardCreateRequest();
    Board board = createBoard(club.getId(), testUser.getUserId(), boardCreateRequest);

    CommentCreateRequest request = commentCreateRequest(1);
    Comment parent = createComment(testUser, club, board, request);

    for (int i = 1; i <= 30; i++) {
      request = CommentCreateRequest
        .builder()
        .content("내용 " + i)
        .parentCommentId(parent.getId())
        .build();

      createComment(testUser, club, board, request);
    }

    // when & then
    mockMvc.perform(get("/api/clubs/{cluId}/boards/{boardId}/comments/{commentId}/child",
                        club.getId(), board.getId(), parent.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data[0].id").exists())
           .andExpect(jsonPath("$.data[0].content").exists())
           .andExpect(jsonPath("$.data[0].depth").exists())
           .andExpect(jsonPath("$.data[0].writer.clubAccountId").exists())
           .andExpect(jsonPath("$.data[0].writer.accountId").exists())
           .andExpect(jsonPath("$.data[0].writer.userId").exists())
           .andExpect(jsonPath("$.data[0].writer.userName").exists())
           .andExpect(jsonPath("$.data[0].writer.role").exists())
           .andExpect(jsonPath("$.data[0].parentCommentId").value(parent.getId()));

  }

}