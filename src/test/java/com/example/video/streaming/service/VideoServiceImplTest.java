package com.example.video.streaming.service;

import static com.example.video.streaming.helper.EngagementEventCreator.createEngagementEvent;
import static com.example.video.streaming.helper.VideoCreator.createBasicVideoResponseDto;
import static com.example.video.streaming.helper.VideoCreator.createVideo;
import static com.example.video.streaming.helper.VideoCreator.createVideoRequestDto;
import static com.example.video.streaming.helper.VideoCreator.createVideoResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.video.streaming.dto.EngagementStatisticsResponseDto;
import com.example.video.streaming.dto.VideoContentResponseDto;
import com.example.video.streaming.dto.VideoListResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.EngagementType;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.VideoRepository;
import com.example.video.streaming.util.MapConvertUtil;
import com.example.video.streaming.util.MapConvertUtilImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Video Service Tests")
@ExtendWith(MockitoExtension.class)
public class VideoServiceImplTest {

  @Mock private FileService fileService;
  @Mock private EngagementEventService engagementEventService;

  @Mock private VideoRepository videoRepository;

  private VideoServiceImpl videoService;

  @BeforeEach
  public void setUp() {
    MapConvertUtil mapConvertUtil = new MapConvertUtilImpl();
    this.videoService =
        new VideoServiceImpl(fileService, engagementEventService, videoRepository, mapConvertUtil);
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

  @Nested
  @DisplayName("Get Video By Id Method")
  class getVideoMetadataAndContentByIdTest {

    @Test
    @DisplayName("when video exists, should return video and save impression")
    public void whenVideoExists_ShouldReturnVideoAndSaveImpression() {
      long videoId = 1L;
      Video mockVideo = createVideo(false);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      when(engagementEventService.saveImpression(mockVideo))
          .thenReturn(createEngagementEvent(mockVideo, EngagementType.IMPRESSION));

      VideoResponseDto expected = createVideoResponseDto(false, Collections.emptyMap());
      VideoResponseDto actual = videoService.getVideoMetadataAndContentById(videoId);
      assertThat(actual).isEqualTo(expected);
      verify(engagementEventService, times(1)).saveImpression(mockVideo);
    }

    @Test
    @DisplayName("when video exists but already soft deleted, should throw EntityNotFoundException")
    public void whenVideoExistsButAlreadySoftDeleted_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      Video mockVideo = createVideo(true);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> videoService.getVideoMetadataAndContentById(videoId));
    }

    @Test
    @DisplayName("when video does not exist, should throw EntityNotFoundException")
    public void whenVideoDoesNotExist_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      when(videoRepository.findById(videoId)).thenReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> videoService.getVideoMetadataAndContentById(videoId));
    }
  }

  @Nested
  @DisplayName("Get Video Content By Id Method")
  class getVideoContentByIdTest {

    @Test
    @DisplayName("when video exists, should return video and save impression")
    public void whenVideoExists_ShouldReturnVideoAndSaveImpression() {
      long videoId = 1L;
      Video mockVideo = createVideo(false);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));

      VideoContentResponseDto expected = new VideoContentResponseDto(mockVideo.getContentLink());
      VideoContentResponseDto actual = videoService.getVideoContentById(videoId);
      assertThat(actual).isEqualTo(expected);
      verify(engagementEventService, times(1)).saveView(mockVideo);
    }

    @Test
    @DisplayName("when video exists but already soft deleted, should throw EntityNotFoundException")
    public void whenVideoExistsButAlreadySoftDeleted_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      Video mockVideo = createVideo(true);
      when(videoRepository.findById(videoId)).thenReturn(Optional.of(mockVideo));
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> videoService.getVideoContentById(videoId));
    }

    @Test
    @DisplayName("when video does not exist, should throw EntityNotFoundException")
    public void whenVideoDoesNotExist_ShouldThrowEntityNotFoundException() {
      long videoId = 1L;
      when(videoRepository.findById(videoId)).thenReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> videoService.getVideoContentById(videoId));
    }
  }

  @Nested
  @DisplayName("List all available videos Method")
  class getListAllAvailableVideosTest {

    @Test
    @DisplayName("when video exists, should return video and save impression")
    public void whenVideoExists_ShouldReturnVideoAndSaveImpression() {
      when(videoRepository.findAllByIsDeleted(false)).thenReturn(List.of(createVideo(false)));

      VideoListResponseDto expected =
          VideoListResponseDto.builder().videos(List.of(createBasicVideoResponseDto())).build();
      VideoListResponseDto actual = videoService.getAvailableVideos();
      assertThat(actual).isEqualTo(expected);
    }
  }

  @Nested
  @DisplayName("Search Videos Method")
  class SearchVideosTest {

    @Test
    @DisplayName("when searching for videos, should return matching videos")
    public void whenSearchingForVideos_ShouldReturnMatchingVideos() {
      VideoRequestDto request = new VideoRequestDto();
      List<Video> mockVideos = List.of(createVideo(false));
      when(videoRepository.findAll(any(Specification.class))).thenReturn(mockVideos);

      VideoListResponseDto expected =
          VideoListResponseDto.builder().videos(List.of(createBasicVideoResponseDto())).build();

      VideoListResponseDto foundVideos = videoService.searchVideos(request);
      assertThat(foundVideos).isEqualTo(expected);
      verify(videoRepository, times(1)).findAll(any(Specification.class));
    }
  }

  @Nested
  @DisplayName("Retrieve Engagement Statistic Method")
  class RetrieveEngagementStatisticTest {

    @Test
    @DisplayName("when searching for videos, should return matching videos")
    public void whenRetrieveEngagementStatistic_ShouldReturnEngazementStatics() {
      Video mockVideo = createVideo(false);
      List<EngagementEvent> mockEngagementEvent =
          List.of(
              createEngagementEvent(mockVideo, EngagementType.IMPRESSION),
              createEngagementEvent(mockVideo, EngagementType.IMPRESSION),
              createEngagementEvent(mockVideo, EngagementType.IMPRESSION),
              createEngagementEvent(mockVideo, EngagementType.IMPRESSION),
              createEngagementEvent(mockVideo, EngagementType.VIEW),
              createEngagementEvent(mockVideo, EngagementType.VIEW));
      when(videoRepository.findById(mockVideo.getVideoId())).thenReturn(Optional.of(mockVideo));
      when(engagementEventService.getEngagementStatistics(mockVideo.getVideoId()))
          .thenReturn(mockEngagementEvent);

      EngagementStatisticsResponseDto expected =
          new EngagementStatisticsResponseDto(mockVideo.getVideoId(), 4, 2);
      EngagementStatisticsResponseDto actual =
          videoService.getEngagementStatistics(mockVideo.getVideoId());
      assertThat(actual).isEqualTo(expected);
    }
  }
}
