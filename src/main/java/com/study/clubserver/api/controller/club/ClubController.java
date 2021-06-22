package com.study.clubserver.api.controller.club;

import static com.study.clubserver.api.common.ApiResult.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.study.clubserver.api.common.ApiResult;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.club.ClubDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.Club;
import com.study.clubserver.domain.club.ClubService;
import com.study.clubserver.security.CurrentAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
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
    WebMvcLinkBuilder selfLinkBuilder = linkTo(ClubController.class);
    return ResponseEntity.created(selfLinkBuilder.toUri())
                         .body(
                           OK(new ClubDto(club))
                         );
  }

}
