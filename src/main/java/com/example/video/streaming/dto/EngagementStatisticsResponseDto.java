package com.example.video.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EngagementStatisticsResponseDto {
  private long videoId;
  private int impressionsCount;
  private int viewsCount;
}
