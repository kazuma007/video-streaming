package com.example.video.streaming.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.video.streaming.config.ApplicationProperties;
import com.example.video.streaming.exception.FileStorageException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

  @Autowired private ResourceLoader resourceLoader;
  @Mock private ApplicationProperties applicationProperties;

  private FileServiceImpl fileService;

  @BeforeEach
  public void setUp() {
    when(applicationProperties.getFileStorageLocation()).thenReturn("./storage");
    fileService = new FileServiceImpl(applicationProperties);
  }

  @Test
  @DisplayName("storeFile function should save the file and return the file name")
  public void storeFile_whenFileIsNotEmpty_ShouldSaveFileAndReturnFileName() throws IOException {
    Resource videoResource = resourceLoader.getResource("classpath:static/test-video.mov");
    byte[] videoBytes = Files.readAllBytes(videoResource.getFile().toPath());
    String originalFileName = videoResource.getFilename();
    MultipartFile mockFile = mock(MultipartFile.class);
    when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
    when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(videoBytes));
    String storedFileName = fileService.storeFile(mockFile);
    assertThat(storedFileName).isEqualTo(originalFileName);
  }

  @Test
  @DisplayName("storeFile function should throw FileStorageException when the file is empty")
  public void storeFile_whenFileIsNull_ShouldThrowFileStorageException() throws IOException {
    MultipartFile mockFile = mock(MultipartFile.class);
    when(mockFile.isEmpty()).thenReturn(true);
    assertThrows(FileStorageException.class, () -> fileService.storeFile(mockFile));
  }

  @Test
  @DisplayName("storeFile function should throw FileStorageException when the file name is null")
  public void storeFile_whenFileNameIsNull_ShouldThrowFileStorageException() throws IOException {
    MultipartFile mockFile = mock(MultipartFile.class);
    when(mockFile.getOriginalFilename()).thenReturn(null);
    assertThrows(FileStorageException.class, () -> fileService.storeFile(mockFile));
  }
}
