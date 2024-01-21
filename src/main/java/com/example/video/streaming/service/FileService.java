package com.example.video.streaming.service;

import org.springframework.web.multipart.MultipartFile;

interface FileService {
  String storeFile(MultipartFile file);
}
