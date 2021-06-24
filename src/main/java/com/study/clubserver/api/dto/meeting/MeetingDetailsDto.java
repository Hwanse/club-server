package com.study.clubserver.api.dto.meeting;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingDetailsDto {

  private MeetingDto meeting;

  private List<MeetingEntryDto> participants;

  public MeetingDetailsDto(MeetingDto meeting, List<MeetingEntryDto> participants) {
    this.meeting = meeting;
    this.participants = participants;
  }
}
