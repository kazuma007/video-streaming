package com.example.video.streaming.service;

import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.VideoRepository;
import com.example.video.streaming.util.MapConvertUtil;
import org.springframework.web.multipart.MultipartFile;

public class VideoServiceImpl implements VideoService {
  private final FileService fileService;
  private final VideoRepository videoRepository;

  private final MapConvertUtil mapConvertUtil;

  public VideoServiceImpl(
      FileService fileService, VideoRepository videoRepository, MapConvertUtil mapConvertUtil) {
    this.fileService = fileService;
    this.videoRepository = videoRepository;
    this.mapConvertUtil = mapConvertUtil;
  }

  @Override
  public VideoResponseDto saveVideo(MultipartFile file) {
    String filepath = fileService.storeFile(file);
    Video video = Video.builder().contentLink(filepath).build();
    Video savedVideo = videoRepository.save(video);
    return mapConvertUtil.videoToVideoResponseDto(savedVideo);
  }
}
