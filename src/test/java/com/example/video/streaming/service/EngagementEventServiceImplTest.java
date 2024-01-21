package com.example.video.streaming.service;

import static com.example.video.streaming.helper.EngagementEventCreator.createEngagementEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.EngagementType;
import com.example.video.streaming.model.Video;
import com.example.video.streaming.repository.EngagementEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Engagement Service Tests")
@ExtendWith(MockitoExtension.class)
public class EngagementEventServiceImplTest {

  @Mock private EngagementEventRepository engagementEventRepository;

  @InjectMocks private EngagementEventServiceImpl engagementEventService;

  @Test
  @DisplayName("when saving impression, should save engagement event with IMPRESSION type")
  public void whenSaveImpression_thenEngagementEventIsSaved() {
    Video video = Video.builder().videoId(1L).build();
    EngagementEvent mockEvent = createEngagementEvent(video, EngagementType.IMPRESSION);

    when(engagementEventRepository.save(any(EngagementEvent.class))).thenReturn(mockEvent);

    EngagementEvent actual = engagementEventService.saveImpression(video);
    assertThat(actual.getEventType()).isEqualTo(EngagementType.IMPRESSION.name().toLowerCase());
    assertThat(actual.getVideo().getVideoId()).isEqualTo(video.getVideoId());
    verify(engagementEventRepository, times(1)).save(any(EngagementEvent.class));
  }

  @Test
  @DisplayName("when saving view, should save engagement event with VIEW type")
  public void whenSaveView_thenEngagementEventIsSaved() {
    Video video = Video.builder().videoId(1L).build();
    EngagementEvent mockEvent = createEngagementEvent(video, EngagementType.VIEW);

    when(engagementEventRepository.save(any(EngagementEvent.class))).thenReturn(mockEvent);

    EngagementEvent actual = engagementEventService.saveImpression(video);
    assertThat(actual.getEventType()).isEqualTo(EngagementType.VIEW.name().toLowerCase());
    assertThat(actual.getVideo().getVideoId()).isEqualTo(video.getVideoId());
    verify(engagementEventRepository, times(1)).save(any(EngagementEvent.class));
  }
}
