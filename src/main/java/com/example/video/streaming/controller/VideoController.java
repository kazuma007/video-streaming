package com.example.video.streaming.controller;

import com.example.video.streaming.dto.VideoContentResponseDto;
import com.example.video.streaming.dto.VideoListResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PutMapping("/{videoId}")
  public ResponseEntity<VideoResponseDto> updateVideo(
      @PathVariable long videoId, @RequestBody VideoRequestDto videoRequestDto) {
    VideoResponseDto response = videoService.updateVideo(videoId, videoRequestDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{videoId}")
  public ResponseEntity<Void> delistVideo(@PathVariable long videoId) {
    videoService.delistVideo(videoId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{videoId}")
  public ResponseEntity<VideoResponseDto> getVideo(@PathVariable long videoId) {
    VideoResponseDto response = videoService.getVideoMetadataAndContentById(videoId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{videoId}/play")
  public ResponseEntity<VideoContentResponseDto> playVideo(@PathVariable long videoId) {
    VideoContentResponseDto response = videoService.getVideoContentById(videoId);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<VideoListResponseDto> listAvailableVideos() {
    VideoListResponseDto response = videoService.getAvailableVideos();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<VideoListResponseDto> searchVideos(VideoRequestDto request) {
    VideoListResponseDto response = videoService.searchVideos(request);
    return ResponseEntity.ok(response);
  }
}
