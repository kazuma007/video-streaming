package com.example.video.streaming.repository.specifications;

import com.example.video.streaming.model.Video;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VideoSpecifications {

  public static Specification<Video> hasTitle(String title) {
    return queryForString("title", title);
  }

  public static Specification<Video> hasSynopsis(String synopsis) {
    return queryForString("synopsis", synopsis);
  }

  public static Specification<Video> hasDirector(String director) {
    return queryForString("director", director);
  }

  public static Specification<Video> hasActor(String actor) {
    return queryForString("actor", actor);
  }

  public static Specification<Video> hasGenre(String genre) {
    return queryForString("genre", genre);
  }

  public static Specification<Video> hasYearOfRelease(Integer yearOfRelease) {
    return queryForInteger("yearOfRelease", yearOfRelease);
  }

  public static Specification<Video> hasRunningTime(Integer runningTime) {
    return queryForInteger("runningTime", runningTime);
  }

  private static Specification<Video> queryForString(String property, String value) {
    return (video, cq, cb) -> {
      if (!StringUtils.hasLength(value)) {
        return cb.isTrue(cb.literal(true));
      }
      return cb.like(cb.lower(video.get(property)), "%" + value.toLowerCase() + "%");
    };
  }

  private static Specification<Video> queryForInteger(String property, Integer value) {
    return (video, cq, cb) -> {
      if (value == null) {
        return cb.isTrue(cb.literal(true));
      }
      return cb.equal(video.get(property), value);
    };
  }
}
