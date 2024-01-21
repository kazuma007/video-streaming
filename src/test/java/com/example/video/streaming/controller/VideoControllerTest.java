package com.example.video.streaming.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.service.VideoService;
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
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
}
