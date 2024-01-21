package com.example.video.streaming.service;

import static com.example.video.streaming.helper.VideoCreator.createVideo;
import static com.example.video.streaming.helper.VideoCreator.createVideoRequestDto;
import static com.example.video.streaming.helper.VideoCreator.createVideoResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.VideoRepository;
import com.example.video.streaming.util.MapConvertUtil;
import com.example.video.streaming.util.MapConvertUtilImpl;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Video Service Tests")
@ExtendWith(MockitoExtension.class)
public class VideoServiceImplTest {

  @Mock private FileService fileService;

  @Mock private VideoRepository videoRepository;

  private VideoServiceImpl videoService;

  @BeforeEach
  public void setUp() {
    MapConvertUtil mapConvertUtil = new MapConvertUtilImpl();
    this.videoService = new VideoServiceImpl(fileService, videoRepository, mapConvertUtil);
  }

  @Nested
  @DisplayName("Save Video Method")
  class SaveVideoTest {

    @Test
    @DisplayName("when saving a video, should return saved video")
    public void whenSavingVideo_ShouldReturnSavedVideo() {
      MultipartFile mockFile = mock(MultipartFile.class);
      String filePath = "test-video.mp4";
      when(fileService.storeFile(mockFile)).thenReturn(filePath);

      Video videoToSave = Video.builder().contentLink(filePath).build();
      when(videoRepository.save(any(Video.class))).thenReturn(videoToSave);

      VideoResponseDto savedVideo = videoService.saveVideo(mockFile);
      assertThat(savedVideo.getContentLink()).isEqualTo(filePath);
      verify(videoRepository, times(1)).save(any(Video.class));
    }
  }

  @Nested
  @DisplayName("Update Video Method")
  class UpdateVideoTest {

    @Test
    @DisplayName("when updating a video, should update and return video")
    public void whenUpdatingVideo_ShouldUpdateAndReturnVideo() {
      long videoId = 1L;
      Video mockVideo = createVideo(false);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      when(videoRepository.save(any(Video.class))).thenReturn(mockVideo);

      VideoRequestDto request = createVideoRequestDto();
      VideoResponseDto expected = createVideoResponseDto(false, Collections.emptyMap());
      VideoResponseDto updatedVideo = videoService.updateVideo(videoId, request);
      verify(videoRepository, times(1)).save(mockVideo);
      assertThat(updatedVideo).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName(
        "when updating a video which already soft deleted, should throw EntityNotFoundException")
    public void whenUpdatingVideoWhichAlreadySoftDeleted_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      VideoRequestDto videoRequestDto = new VideoRequestDto();
      Video mockVideo = createVideo(true);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> videoService.updateVideo(videoId, videoRequestDto));
      verify(videoRepository, times(0)).save(mockVideo);
    }

    @Test
    @DisplayName("when updating a video which does not exist, should throw EntityNotFoundException")
    public void whenUpdatingVideoWhichDoesNotExist_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      VideoRequestDto videoRequestDto = new VideoRequestDto();
      when(videoRepository.findById(videoId)).thenReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> videoService.updateVideo(videoId, videoRequestDto));
    }
  }

  @Nested
  @DisplayName("Delist Video Method")
  class DelistVideoTest {

    @Test
    @DisplayName("when delisting a video, should mark video as deleted")
    public void whenDelistingVideo_ShouldMarkVideoAsDeleted() {
      long videoId = 1L;
      Video mockVideo = createVideo(false);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      videoService.delistVideo(videoId);
      verify(videoRepository, times(1)).save(mockVideo);
    }

    @Test
    @DisplayName(
        "when delisting a video which does not exist, should throw EntityNotFoundException")
    public void whenDelistingVideoWhichDoesNotExist_ShouldMarkVideoAsDeleted() {
      long videoId = 1L;
      when(videoRepository.findById(videoId)).thenReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> videoService.delistVideo(videoId));
    }
  }
}
