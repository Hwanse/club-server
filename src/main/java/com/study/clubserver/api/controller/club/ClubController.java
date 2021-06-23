package com.study.clubserver.api.controller.club;

import static com.study.clubserver.api.dto.ApiResult.OK;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.http.ResponseEntity;
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
    @Valid @RequestBody ClubCreateRequest request) {
    Club club = clubService.createClub(account, request);

    WebMvcLinkBuilder selfLinkBuilder = linkTo(
      methodOn(ClubController.class).getClubMembersDetails(account, club.getId())
    );

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
    @PageableDefault(sort = "createAT", direction = Direction.DESC) Pageable pageable) {
    return OK(clubService.getClubPage(pageable));
  }

}
