package com.study.clubserver.api.dto.meeting;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingCreateRequest {

  @NotEmpty
  private String title;

  @NotEmpty
  private String description;

  @NotNull
  private LocalDateTime meetingStartTime;

  @NotNull
  private LocalDateTime meetingEndTime;

  @Min(2)
  @Max(10)
  private int limitEntryCount;

  @NotEmpty
  private String meetingAddress;

}
