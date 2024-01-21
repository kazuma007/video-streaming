package com.example.video.streaming.helper;

import com.example.video.streaming.dto.BasicVideoResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.Video;
import java.util.Map;

public class VideoCreator {
  public static Video createVideo(boolean isDeleted) {
    return Video.builder()
        .contentLink("test.jpg")
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
        .contentLink("test.jpg")
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
    VideoRequestDto videoRequestDto = new VideoRequestDto();
    videoRequestDto.setTitle("test-title");
    videoRequestDto.setActor("test-actor");
    videoRequestDto.setGenre("test-genre");
    videoRequestDto.setDirector("test-director");
    videoRequestDto.setRunningTime(5);
    videoRequestDto.setSynopsis("test-synopsis");
    videoRequestDto.setYearOfRelease(2023);
    return videoRequestDto;
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
