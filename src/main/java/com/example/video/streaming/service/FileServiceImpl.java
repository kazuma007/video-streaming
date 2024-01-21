package com.example.video.streaming.service;

import com.example.video.streaming.config.ApplicationProperties;
import com.example.video.streaming.exception.FileStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
  private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

  private final Path fileStorageLocation;

  @Autowired
  public FileServiceImpl(ApplicationProperties applicationProperties) {
    this.fileStorageLocation =
        Paths.get(applicationProperties.getFileStorageLocation()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception e) {
      logger.error("File Storage not created: {}", e.getMessage());
      throw new FileStorageException(
          "Could not create the directory where the uploaded files will be stored.", e);
    }
  }

  /**
   * Stores a file to the local directory. However, a file should be store in a cloud storage such
   * as S3 using an asynchronous processing.
   *
   * @param file
   * @return
   */
  @Override
  public String storeFile(MultipartFile file) {
    if (file.isEmpty() || file.getOriginalFilename() == null) {
      logger.error("File to store empty file");
      throw new FileStorageException("Failed to store empty file.");
    }

    try {
      String filename = file.getOriginalFilename();
      Path targetLocation = this.fileStorageLocation.resolve(filename);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return filename;
    } catch (IOException e) {
      logger.error("File not stored: {}", e.getMessage());
      throw new FileStorageException("Failed to store file.", e);
    }
  }
}
