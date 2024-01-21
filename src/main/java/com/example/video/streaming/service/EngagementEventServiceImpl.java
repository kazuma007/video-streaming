package com.example.video.streaming.service;

import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.EngagementType;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.EngagementEventRepository;
import org.springframework.stereotype.Service;

@Service
class EngagementEventServiceImpl implements EngagementEventService {
  private final EngagementEventRepository engagementEventRepository;

  EngagementEventServiceImpl(EngagementEventRepository engagementEventRepository) {
    this.engagementEventRepository = engagementEventRepository;
  }

  @Override
  public EngagementEvent saveImpression(Video video) {
    return saveEngagementEvent(video, EngagementType.IMPRESSION);
  }

  @Override
  public EngagementEvent saveView(Video video) {
    return saveEngagementEvent(video, EngagementType.VIEW);
  }

  private EngagementEvent saveEngagementEvent(Video video, EngagementType eventType) {
    EngagementEvent event =
        EngagementEvent.builder().video(video).eventType(eventType.name().toLowerCase()).build();
    return engagementEventRepository.save(event);
  }
}
