package com.example.video.streaming.service;

import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
  VideoResponseDto saveVideo(MultipartFile file);

  VideoResponseDto updateVideo(long videoId, VideoRequestDto videoRequestDto);

  void delistVideo(long videoId);
}
