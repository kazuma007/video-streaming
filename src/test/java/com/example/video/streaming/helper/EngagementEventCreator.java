package com.example.video.streaming.helper;

import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.EngagementType;
import com.example.video.streaming.model.Video;

public class EngagementEventCreator {
  public static EngagementEvent createEngagementEvent(Video video, EngagementType type) {
    return EngagementEvent.builder().video(video).eventType(type.name().toLowerCase()).build();
  }
}
