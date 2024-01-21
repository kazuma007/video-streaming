package com.example.video.streaming.repository;

import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EngagementEventRepository
    extends JpaRepository<EngagementEvent, Long>, JpaSpecificationExecutor<Video> {}
