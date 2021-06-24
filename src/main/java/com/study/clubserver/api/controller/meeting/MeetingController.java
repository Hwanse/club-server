package com.study.clubserver.api.controller.meeting;

import static com.study.clubserver.api.dto.ApiResult.OK;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.study.clubserver.api.dto.ApiResult;
import com.study.clubserver.api.dto.meeting.MeetingCreateRequest;
import com.study.clubserver.api.dto.meeting.MeetingDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.meeting.Meeting;
import com.study.clubserver.domain.meeting.MeetingService;
import com.study.clubserver.security.CurrentAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/meetings")
public class MeetingController {

  private final MeetingService meetingService;

  @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity createMeeting(@CurrentAccount Account account, @PathVariable Long clubId,
    @Valid @RequestBody MeetingCreateRequest request) {
    Meeting meeting = meetingService.createMeeting(account, clubId, request);

    WebMvcLinkBuilder linkBuilder = linkTo(MeetingController.class, clubId).slash(meeting.getId());
    return ResponseEntity.created(linkBuilder.toUri())
                         .body(
                           OK(new MeetingDto(meeting))
                         );
  }

}
