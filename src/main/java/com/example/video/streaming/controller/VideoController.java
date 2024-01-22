package com.example.video.streaming.controller;

import com.example.video.streaming.dto.EngagementStatisticsResponseDto;
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

  /**
   * Publish a video
   *
   * @param file
   * @return the saved video
   */
  @PostMapping
  public ResponseEntity<VideoResponseDto> saveVideo(@RequestParam("file") MultipartFile file) {
    VideoResponseDto response = videoService.saveVideo(file);
    return ResponseEntity.ok(response);
  }

  /**
   * Add and Edit the metadata associated with the video some examples of metadata are: Title,
   * Synopsis, Director, Cast, Year of Release, Genre, Running time)
   *
   * @param videoId
   * @param videoRequestDto
   * @return the saved video and metadata
   */
  @PutMapping("/{videoId}")
  public ResponseEntity<VideoResponseDto> updateVideo(
      @PathVariable long videoId, @RequestBody VideoRequestDto videoRequestDto) {
    VideoResponseDto response = videoService.updateVideo(videoId, videoRequestDto);
    return ResponseEntity.ok(response);
  }

  /**
   * Delist (soft delete) a video and its associated metadata
   *
   * @param videoId
   * @return
   */
  @DeleteMapping("/{videoId}")
  public ResponseEntity<Void> delistVideo(@PathVariable long videoId) {
    videoService.delistVideo(videoId);
    return ResponseEntity.ok().build();
  }

  /**
   * Load a video
   *
   * @param videoId
   * @return the video metadata and the corresponding content.
   */
  @GetMapping("/{videoId}")
  public ResponseEntity<VideoResponseDto> getVideo(@PathVariable long videoId) {
    VideoResponseDto response = videoService.getVideoMetadataAndContentById(videoId);
    return ResponseEntity.ok(response);
  }

  /**
   * Play a video
   *
   * @param videoId
   * @return the content related to a video
   */
  @GetMapping("/{videoId}/play")
  public ResponseEntity<VideoContentResponseDto> playVideo(@PathVariable long videoId) {
    VideoContentResponseDto response = videoService.getVideoContentById(videoId);
    return ResponseEntity.ok(response);
  }

  /**
   * List all available videos
   *
   * @return a subset of the video metadata such as: Title, Director, Main Actor, Genre and Running
   *     Time.
   */
  @GetMapping
  public ResponseEntity<VideoListResponseDto> listAvailableVideos() {
    VideoListResponseDto response = videoService.getAvailableVideos();
    return ResponseEntity.ok(response);
  }

  /**
   * Search for videos
   *
   * @param request search/query criteria
   * @return a subset of the video metadata
   */
  @GetMapping("/search")
  public ResponseEntity<VideoListResponseDto> searchVideos(VideoRequestDto request) {
    VideoListResponseDto response = videoService.searchVideos(request);
    return ResponseEntity.ok(response);
  }

  /**
   * Retrieve the engagement statistic for a video
   *
   * @param videoId
   * @return the engagement statistic
   */
  @GetMapping("/{videoId}/engagements")
  public ResponseEntity<EngagementStatisticsResponseDto> getEngagementStatistics(
      @PathVariable long videoId) {
    EngagementStatisticsResponseDto response = videoService.getEngagementStatistics(videoId);
    return ResponseEntity.ok(response);
  }
}
