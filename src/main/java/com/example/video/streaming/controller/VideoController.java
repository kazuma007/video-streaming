package com.example.video.streaming.controller;

import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
  private final VideoService videoService;

  public VideoController(VideoService videoService) {
    this.videoService = videoService;
  }

  @PostMapping
  public ResponseEntity<VideoResponseDto> saveVideo(@RequestParam("file") MultipartFile file) {
    VideoResponseDto response = videoService.saveVideo(file);
    return ResponseEntity.ok(response);
  }
}
