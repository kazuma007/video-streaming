package com.example.video.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicVideoResponseDto {
  private long videoId;
  private String title;
  private String synopsis;
  private String director;
  private Short yearOfRelease;
  private String genre;
  private String actor;
  private Short runningTime;
}
