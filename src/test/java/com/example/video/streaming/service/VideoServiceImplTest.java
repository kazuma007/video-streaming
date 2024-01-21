package com.example.video.streaming.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.VideoRepository;
import com.example.video.streaming.util.MapConvertUtil;
import com.example.video.streaming.util.MapConvertUtilImpl;
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
}
