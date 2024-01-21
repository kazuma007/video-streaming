package com.example.video.streaming.service;

import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.VideoRepository;
import com.example.video.streaming.util.MapConvertUtil;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoServiceImpl implements VideoService {
  private final FileService fileService;
  private final EngagementEventService engagementEventService;
  private final VideoRepository videoRepository;

  private final MapConvertUtil mapConvertUtil;

  public VideoServiceImpl(
      FileService fileService,
      EngagementEventService engagementEventService,
      VideoRepository videoRepository,
      MapConvertUtil mapConvertUtil) {
    this.fileService = fileService;
    this.engagementEventService = engagementEventService;
    this.videoRepository = videoRepository;
    this.mapConvertUtil = mapConvertUtil;
  }

  private Video findActiveVideoById(long videoId) {
    return videoRepository
        .findById(videoId)
        .filter(video -> !video.isDeleted())
        .orElseThrow(() -> new EntityNotFoundException("Video not found with ID: " + videoId));
  }

  @Override
  public VideoResponseDto saveVideo(MultipartFile file) {
    String filepath = fileService.storeFile(file);
    Video video = Video.builder().contentLink(filepath).build();
    Video savedVideo = videoRepository.save(video);
    return mapConvertUtil.videoToVideoResponseDto(savedVideo);
  }

  @Override
  public VideoResponseDto updateVideo(long videoId, VideoRequestDto videoRequestDto) {
    Video video = findActiveVideoById(videoId);
    mapConvertUtil.updateVideoFromDto(videoRequestDto, video);
    Video savedVideo = videoRepository.save(video);
    return mapConvertUtil.videoToVideoResponseDto(savedVideo);
  }

  @Override
  public void delistVideo(long videoId) {
    Video video = findActiveVideoById(videoId);
    video.setDeleted(true);
    videoRepository.save(video);
  }

  @Override
  @Transactional
  public VideoResponseDto getVideoMetadataAndContentById(long videoId) {
    Video video = findActiveVideoById(videoId);
    EngagementEvent engagementEvent = engagementEventService.saveImpression(video);

    // update the engagementEvents since the event has been inserted
    List<EngagementEvent> updatedEvents =
        Stream.concat(video.getEngagementEvents().stream(), Stream.of(engagementEvent)).toList();
    video.setEngagementEvents(updatedEvents);

    return mapConvertUtil.videoToVideoResponseDto(video);
  }
}
