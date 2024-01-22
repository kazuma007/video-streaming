package com.example.video.streaming.controller;

import static com.example.video.streaming.helper.VideoCreator.createBasicVideoResponseDto;
import static com.example.video.streaming.helper.VideoCreator.createVideoRequestDto;
import static com.example.video.streaming.helper.VideoCreator.createVideoResponseDto;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.video.streaming.dto.BasicVideoResponseDto;
import com.example.video.streaming.dto.EngagementStatisticsResponseDto;
import com.example.video.streaming.dto.VideoContentResponseDto;
import com.example.video.streaming.dto.VideoListResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.EngagementType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VideoControllerIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private ResourceLoader resourceLoader;
  @Autowired private JdbcTemplate jdbcTemplate;

  private ResponseEntity<VideoListResponseDto> callSearchVideos(VideoRequestDto request) {
    String urlTemplate =
        UriComponentsBuilder.fromPath("/api/v1/videos/search")
            .queryParam("title", request.getTitle())
            .queryParam("synopsis", request.getSynopsis())
            .queryParam("director", request.getDirector())
            .queryParam("actor", request.getActor())
            .queryParam("year_of_release", request.getYearOfRelease())
            .queryParam("genre", request.getGenre())
            .queryParam("running_time", request.getRunningTime())
            .toUriString();

    return restTemplate.getForEntity(urlTemplate, VideoListResponseDto.class);
  }

  private ResponseEntity<VideoResponseDto> callGetVideo(long videoId) {
    return restTemplate.getForEntity("/api/v1/videos/" + videoId, VideoResponseDto.class);
  }

  private ResponseEntity<VideoContentResponseDto> callPlayVideo(long videoId) {
    return restTemplate.getForEntity(
        "/api/v1/videos/" + videoId + "/play", VideoContentResponseDto.class);
  }

  private ResponseEntity<EngagementStatisticsResponseDto> callEngagementStatistics(long videoId) {
    return restTemplate.getForEntity(
        "/api/v1/videos/" + videoId + "/engagements", EngagementStatisticsResponseDto.class);
  }

  private ResponseEntity<Void> callDelistVideo(long videoId) {
    return restTemplate.exchange("/api/v1/videos/" + videoId, HttpMethod.DELETE, null, Void.class);
  }

  private ResponseEntity<VideoResponseDto> callSaveVideo() {
    Resource videoResource = resourceLoader.getResource("classpath:static/test-video.mov");
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", videoResource);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    return restTemplate.postForEntity("/api/v1/videos", requestEntity, VideoResponseDto.class);
  }

  private ResponseEntity<VideoResponseDto> callUpdateVideo(
      long videoId, VideoRequestDto videoRequestDto) {
    HttpEntity<VideoRequestDto> requestEntity = new HttpEntity<>(videoRequestDto);
    return restTemplate.exchange(
        "/api/v1/videos/" + videoId, HttpMethod.PUT, requestEntity, VideoResponseDto.class);
  }

  private ResponseEntity<VideoListResponseDto> callListAllVideo() {
    return restTemplate.getForEntity("/api/v1/videos", VideoListResponseDto.class);
  }

  @BeforeEach
  void tearDown() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "engagement_events", "videos");

    // add a sample item to Video table
    ResponseEntity<VideoResponseDto> savedVideo = callSaveVideo();
    long videoId = Objects.requireNonNull(savedVideo.getBody()).getVideoId();
    callUpdateVideo(videoId, createVideoRequestDto());
  }

  @Test
  @DisplayName("when list all available videos, should return all videos which exists")
  public void whenPublishVideoAndMetadata_returnsVideoResponse() {
    // Publish a video
    ResponseEntity<VideoResponseDto> savedVideo = callSaveVideo();
    assertThat(savedVideo.getBody())
        .usingRecursiveComparison()
        .ignoringFields("videoId", "createdAt", "updatedAt")
        .isEqualTo(
            VideoResponseDto.builder()
                .contentLink("test-video.mov")
                .engagementEventsCount(Collections.emptyMap())
                .build());

    // Add and Edit the metadata associated with the video
    long videoId = Objects.requireNonNull(savedVideo.getBody()).getVideoId();
    ResponseEntity<VideoResponseDto> updatedVideo =
        callUpdateVideo(videoId, createVideoRequestDto());
    assertThat(updatedVideo.getBody())
        .usingRecursiveComparison()
        .ignoringFields("videoId", "createdAt", "updatedAt")
        .isEqualTo(createVideoResponseDto(false, emptyMap()));
  }

  @Test
  @DisplayName("when list all available videos, should return all videos which exists")
  public void whenDelistingVideo_shouldNotReturnDelistedVideo() {
    // List all available videos
    ResponseEntity<VideoListResponseDto> callListAllVideoResponse = callListAllVideo();
    assertThat(callListAllVideoResponse.getBody())
        .usingRecursiveComparison()
        .ignoringFields("videos.videoId")
        .isEqualTo(
            VideoListResponseDto.builder().videos(List.of(createBasicVideoResponseDto())).build());

    long videoId = callListAllVideoResponse.getBody().getVideos().get(0).getVideoId();

    // Delist (soft delete) a video and its associated metadata
    callDelistVideo(videoId);

    // List all available videos
    ResponseEntity<VideoListResponseDto> callListAllVideoEmptyResponse = callListAllVideo();
    assertThat(callListAllVideoEmptyResponse.getBody())
        .isEqualTo(VideoListResponseDto.builder().videos(List.of()).build());
  }

  @Test
  @DisplayName(
      "when retrieving the engagement statistic for a video, should return the statistic for impressions and views")
  public void whenRetrievingEngagementStatistic_shouldReturnStatistic() {
    // List all available videos to know the video id
    ResponseEntity<VideoListResponseDto> callListAllVideoResponse = callListAllVideo();
    long videoId = callListAllVideoResponse.getBody().getVideos().get(0).getVideoId();

    // Load a video – return the video metadata and the corresponding content.
    ResponseEntity<VideoResponseDto> callGetVideoResponse = callGetVideo(videoId);
    assertThat(callGetVideoResponse.getBody())
        .usingRecursiveComparison()
        .ignoringFields("videoId", "createdAt", "updatedAt")
        .isEqualTo(
            createVideoResponseDto(
                false, Map.of(EngagementType.IMPRESSION.name().toLowerCase(), 1L)));

    // Play a video – return the content related to a video.
    ResponseEntity<VideoContentResponseDto> callPlayVideoResponse = callPlayVideo(videoId);
    VideoContentResponseDto videoContentResponseDto = new VideoContentResponseDto("test-video.mov");
    assertThat(callPlayVideoResponse.getBody())
        .usingRecursiveComparison()
        .isEqualTo(videoContentResponseDto);

    // Retrieve the engagement statistic for a video.
    // Since the video was loaded and played already, the engagement statistic should contain 1 for
    // both impression and view.
    ResponseEntity<EngagementStatisticsResponseDto> callEngagementStatisticsResponse =
        callEngagementStatistics(videoId);
    assertThat(callEngagementStatisticsResponse.getBody())
        .isEqualTo(new EngagementStatisticsResponseDto(videoId, 1, 1));
  }

  @ParameterizedTest
  @MethodSource("videoSearchProvider")
  @DisplayName(
      "when searching for videos based on some search/query criteria, should return the matched video")
  public void whenSearchingVideosBasedOnQuery_shouldReturnMatchedVideos(
      VideoRequestDto request, List<BasicVideoResponseDto> expectedBasicVideoResponseDto) {
    // Search for videos based on some search/query criteria (e.g.: Movies directed by a specific
    // director)
    ResponseEntity<VideoListResponseDto> callSearchVideosResponse = callSearchVideos(request);
    assertThat(callSearchVideosResponse.getBody())
        .usingRecursiveComparison()
        .ignoringFields("videos.videoId")
        .isEqualTo(VideoListResponseDto.builder().videos(expectedBasicVideoResponseDto).build());
  }

  private static Stream<Arguments> videoSearchProvider() {
    return Stream.of(
        Arguments.of(
            createVideoRequestDto(),
            List.of(createBasicVideoResponseDto()),
            Arguments.of(VideoRequestDto.builder().actor("no-actor").build(), List.of()),
            Arguments.of(VideoRequestDto.builder().title("no-title").build(), List.of()),
            Arguments.of(VideoRequestDto.builder().genre("no-genre").build(), List.of()),
            Arguments.of(VideoRequestDto.builder().director("no-director").build(), List.of()),
            Arguments.of(VideoRequestDto.builder().synopsis("no-synopsis").build(), List.of()),
            Arguments.of(VideoRequestDto.builder().yearOfRelease(1995).build(), List.of()),
            Arguments.of(VideoRequestDto.builder().runningTime(3000).build(), List.of())));
  }
}
