package com.example.video.streaming.service;

import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.Video;

interface EngagementEventService {
  EngagementEvent saveImpression(Video video);

  EngagementEvent saveView(Video video);
}
