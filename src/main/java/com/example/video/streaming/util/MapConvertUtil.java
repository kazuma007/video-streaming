package com.example.video.streaming.util;

import com.example.video.streaming.dto.VideoRequestDto;
import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.EngagementEvent;
import com.example.video.streaming.model.Video;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MapConvertUtil {

  @Mappings({
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
    @Mapping(source = "deleted", target = "isDeleted"),
    @Mapping(
        source = "engagementEvents",
        target = "engagementEventsCount",
        qualifiedByName = "mapEngagementEvents")
  })
  VideoResponseDto videoToVideoResponseDto(Video video);

  @Named("mapEngagementEvents")
  default Map<String, Long> mapEngagementEvents(List<EngagementEvent> engagementEvents) {
    if (engagementEvents == null) {
      return Collections.emptyMap();
    }
    return engagementEvents.stream()
        .collect(Collectors.groupingBy(EngagementEvent::getEventType, Collectors.counting()));
  }

  @Mapping(target = "videoId", ignore = true)
  @Mapping(target = "contentLink", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "engagementEvents", ignore = true)
  void updateVideoFromDto(VideoRequestDto dto, @MappingTarget Video video);
}
