package com.study.clubserver.api.controller.club;

import static com.study.clubserver.api.dto.ApiResult.ERROR;
import static com.study.clubserver.api.dto.ApiResult.OK;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.study.clubserver.api.dto.ApiResult;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.club.ClubDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.security.CurrentAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

  private final ClubService clubService;

  @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity createClub(@CurrentAccount Account account,
    @Valid @RequestBody ClubCreateRequest request, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
                           .body(
                             ERROR(HttpStatus.BAD_REQUEST, "입력 값 조건에 맞지 않습니다.")
                           );
    }

    Club club = clubService.createClub(account, request);

    WebMvcLinkBuilder selfLinkBuilder = linkTo(ClubController.class).slash(club.getId());
    return ResponseEntity.created(selfLinkBuilder.toUri())
                         .body(
                           OK(new ClubDto(club))
                         );
  }

  @GetMapping("/{clubId}")
  public ApiResult getClubMembersDetails(@CurrentAccount Account account, @PathVariable Long clubId) {
    return OK(clubService.getClubDetails(clubId, account));
  }

  @GetMapping
  public ApiResult queryClubPage(@CurrentAccount Account account,
    @PageableDefault(sort = "createAt", direction = Direction.DESC) Pageable pageable) {
    return OK(clubService.getClubPage(pageable));
  }

  @PostMapping("/{clubId}/join")
  public ApiResult clubJoin(@CurrentAccount Account account, @PathVariable Long clubId) {
    clubService.accountJoinToClub(account, clubId);
    return OK(true);
  }

}
