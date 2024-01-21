package com.example.video.streaming.repository;

import com.example.video.streaming.model.Video;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository
    extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {
  List<Video> findAllByIsDeleted(Boolean isDeleted);
}
