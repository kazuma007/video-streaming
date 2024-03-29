package com.example.video.streaming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequestDto {
  private String title;

  private String synopsis;

  private String director;

  private String actor;

  @JsonProperty("year_of_release")
  private Integer yearOfRelease;

  private String genre;

  @JsonProperty("running_time")
  private Integer runningTime;
}
