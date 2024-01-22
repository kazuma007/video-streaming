package com.example.video.streaming.helper;

import com.example.video.streaming.dto.BasicVideoResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.Video;
import java.util.Map;

public class VideoCreator {
  public static Video createVideo(boolean isDeleted) {
    return Video.builder()
        .contentLink("test-video.mov")
        .title("test-title")
        .actor("test-actor")
        .genre("test-genre")
        .director("test-director")
        .runningTime((short) 5)
        .synopsis("test-synopsis")
        .yearOfRelease((short) 2023)
        .isDeleted(isDeleted)
        .build();
  }

  public static VideoResponseDto createVideoResponseDto(
      boolean isDeleted, Map<String, Long> engagementEventsCount) {
    return VideoResponseDto.builder()
        .contentLink("test-video.mov")
        .title("test-title")
        .actor("test-actor")
        .genre("test-genre")
        .director("test-director")
        .runningTime((short) 5)
        .synopsis("test-synopsis")
        .yearOfRelease((short) 2023)
        .isDeleted(isDeleted)
        .engagementEventsCount(engagementEventsCount)
        .build();
  }

  public static VideoRequestDto createVideoRequestDto() {
    return VideoRequestDto.builder()
        .title("test-title")
        .actor("test-actor")
        .genre("test-genre")
        .director("test-director")
        .runningTime(5)
        .synopsis("test-synopsis")
        .yearOfRelease(2023)
        .build();
  }

  public static BasicVideoResponseDto createBasicVideoResponseDto() {
    return BasicVideoResponseDto.builder()
        .title("test-title")
        .actor("test-actor")
        .genre("test-genre")
        .director("test-director")
        .runningTime((short) 5)
        .synopsis("test-synopsis")
        .yearOfRelease((short) 2023)
        .build();
  }
}
