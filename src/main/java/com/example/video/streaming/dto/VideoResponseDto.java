package com.example.video.streaming.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class VideoResponseDto {
  private long videoId;
  private String title;
  private String synopsis;
  private String director;
  private Short yearOfRelease;
  private String genre;
  private String actor;
  private Short runningTime;
  private String contentLink;
  private boolean isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Map<String, Long> engagementEventsCount;
}
