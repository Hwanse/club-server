package com.study.clubserver.api.dto.club;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
public class ClubCreateRequest {

  @NotEmpty
  private String title;

  @NotEmpty
  private String description;

  @Min(5) @Max(30)
  private int limitMemberCount;

  private String bannerImageUrl;

  @NotEmpty
  private List<Long> interestList;

  @NotEmpty
  private List<Long> zoneList;

}
