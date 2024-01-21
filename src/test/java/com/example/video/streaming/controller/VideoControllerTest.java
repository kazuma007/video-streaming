package com.example.video.streaming.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.video.streaming.dto.BasicVideoResponseDto;
import com.example.video.streaming.dto.EngagementStatisticsResponseDto;
import com.example.video.streaming.dto.VideoContentResponseDto;
import com.example.video.streaming.dto.VideoListResponseDto;
import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.service.VideoService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(controllers = VideoController.class)
class VideoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private VideoService videoService;

  @Nested
  @DisplayName("POST Requests")
  class PostRequests {

    @Test
    @DisplayName("POST /api/v1/videos - Save Video")
    void saveVideo_ShouldReturnVideoResponseDto() throws Exception {
      MockMultipartFile file =
          new MockMultipartFile(
              "file",
              "test-video.mp4",
              MediaType.parseMediaType("video/mp4").toString(),
              "".getBytes());
      VideoResponseDto response = new VideoResponseDto();
      when(videoService.saveVideo(any(MultipartFile.class))).thenReturn(response);

      mockMvc
          .perform(multipart("/api/v1/videos").file(file))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videoId").exists());
    }
  }

  @Nested
  @DisplayName("PUT Requests")
  class PutRequests {

    @Test
    @DisplayName("PUT /api/v1/videos/{videoId} - Update Video")
    void updateVideo_ShouldReturnVideoResponseDto() throws Exception {
      long videoId = 1L;
      VideoResponseDto response = new VideoResponseDto();
      when(videoService.updateVideo(eq(videoId), any(VideoRequestDto.class))).thenReturn(response);

      mockMvc
          .perform(
              put("/api/v1/videos/{videoId}", videoId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{}"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videoId").exists());
    }
  }

  @Nested
  @DisplayName("DELETE Requests")
  class DeleteRequests {

    @Test
    @DisplayName("DELETE /api/v1/videos/{videoId}/delist - Delist Video")
    void delistVideo_ShouldReturnOk() throws Exception {
      long videoId = 1L;
      mockMvc.perform(delete("/api/v1/videos/{videoId}", videoId)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v1/videos/{videoId}/delist - Delist Video")
    void delistVideo_whenEntityDoesNotExist_ShouldReturnNotFound() throws Exception {
      long videoId = 1L;
      doThrow(new EntityNotFoundException("Entity not found"))
          .when(videoService)
          .delistVideo(videoId);
      mockMvc.perform(delete("/api/v1/videos/{videoId}", videoId)).andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET Requests")
  class GetRequests {

    @Test
    @DisplayName("GET /api/v1/videos/{videoId} - Get Video by ID")
    void getVideo_ShouldReturnVideoResponseDto() throws Exception {
      long videoId = 1L;
      VideoResponseDto response = new VideoResponseDto();
      when(videoService.getVideoMetadataAndContentById(videoId)).thenReturn(response);

      mockMvc
          .perform(get("/api/v1/videos/{videoId}", videoId))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videoId").exists());
    }

    @Test
    @DisplayName("GET /api/v1/videos/{videoId} - Get Video by ID (Entity Not Found)")
    void getVideo_whenEntityDoesNotExist_ShouldReturnNotFound() throws Exception {
      long videoId = 1L;
      when(videoService.getVideoMetadataAndContentById(videoId))
          .thenThrow(new EntityNotFoundException("Entity not found"));

      mockMvc.perform(get("/api/v1/videos/{videoId}", videoId)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/videos/{videoId}/play - Play Video")
    void playVideo_ShouldReturnVideoContentResponseDto() throws Exception {
      long videoId = 1L;
      VideoContentResponseDto response = new VideoContentResponseDto("test-video.mp4");
      when(videoService.getVideoContentById(videoId)).thenReturn(response);

      mockMvc
          .perform(get("/api/v1/videos/{videoId}/play", videoId))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.contentLink").exists());
    }

    @Test
    @DisplayName("GET /api/v1/videos/{videoId}/play - Play Video")
    void playVideo_whenEntityDoesNotExist_ShouldReturnNotFound() throws Exception {
      long videoId = 1L;
      when(videoService.getVideoContentById(videoId))
          .thenThrow(new EntityNotFoundException("Entity not found"));
      mockMvc
          .perform(get("/api/v1/videos/{videoId}/play", videoId))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/videos - List Available Videos")
    void listAvailableVideos_ShouldReturnListOfVideos() throws Exception {
      VideoListResponseDto response =
          VideoListResponseDto.builder().videos(List.of(new BasicVideoResponseDto())).build();
      when(videoService.getAvailableVideos()).thenReturn(response);

      mockMvc
          .perform(get("/api/v1/videos"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videos").exists());
    }

    @Test
    @DisplayName(
        "GET /api/v1/videos/search - Search for videos based on some search/query criteria")
    void searchVideos_ShouldReturnListOfVideos() throws Exception {
      VideoRequestDto request = new VideoRequestDto();
      request.setDirector("test-director");

      VideoListResponseDto response =
          VideoListResponseDto.builder().videos(List.of(new BasicVideoResponseDto())).build();
      when(videoService.searchVideos(any(VideoRequestDto.class))).thenReturn(response);

      mockMvc
          .perform(get("/api/v1/videos/search").param("director", "Director Name"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videos").exists());
    }

    @Test
    @DisplayName("GET /api/v1/videos/{videoId}/engagements - Get Engagement Statistics")
    void getEngagementStatistics_ShouldReturnEngagementStatistics() throws Exception {
      long videoId = 1L;
      EngagementStatisticsResponseDto response = new EngagementStatisticsResponseDto();
      when(videoService.getEngagementStatistics(videoId)).thenReturn(response);
      mockMvc
          .perform(get("/api/v1/videos/{videoId}/engagements", videoId))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.videoId").exists());
    }
  }
}
