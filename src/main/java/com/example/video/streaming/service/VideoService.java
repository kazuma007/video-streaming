package com.example.video.streaming.service;

import com.example.video.streaming.dto.EngagementStatisticsResponseDto;
import com.example.video.streaming.dto.VideoContentResponseDto;
import com.example.video.streaming.dto.VideoListResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
  VideoResponseDto saveVideo(MultipartFile file);

  VideoResponseDto updateVideo(long videoId, VideoRequestDto videoRequestDto);

  void delistVideo(long videoId);

  VideoResponseDto getVideoMetadataAndContentById(long videoId);

  VideoContentResponseDto getVideoContentById(long videoId);

  VideoListResponseDto getAvailableVideos();

  VideoListResponseDto searchVideos(VideoRequestDto request);

  EngagementStatisticsResponseDto getEngagementStatistics(long videoId);
}
