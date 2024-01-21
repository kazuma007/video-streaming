package com.example.video.streaming.repository;

import com.example.video.streaming.model.EngagementEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngagementEventRepository extends JpaRepository<EngagementEvent, Long> {
  List<EngagementEvent> findByVideoVideoId(long videoId);
}
