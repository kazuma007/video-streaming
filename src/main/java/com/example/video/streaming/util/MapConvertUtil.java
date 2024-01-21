package com.example.video.streaming.util;

import com.example.video.streaming.dto.VideoResponseDto;
import com.example.video.streaming.model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MapConvertUtil {

  @Mappings({
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss"),
    @Mapping(source = "deleted", target = "deleted"),
  })
  VideoResponseDto videoToVideoResponseDto(Video video);
}
