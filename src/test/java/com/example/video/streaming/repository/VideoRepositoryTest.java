package com.example.video.streaming.repository;

import static com.example.video.streaming.helper.VideoCreator.createVideo;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.video.streaming.model.Video;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("Video Repository Tests")
public class VideoRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private VideoRepository videoRepository;

  @BeforeEach
  public void setUp() {
    entityManager.clear();
  }

  @Nested
  @DisplayName("findAllByIsDeleted Method")
  class FindAllByIsDeleted {

    @Test
    @DisplayName("when searching for non-deleted videos, should return only non-deleted videos")
    public void whenFindAllByIsDeleted_thenReturnNonDeletedVideos() {
      Video video1 = createVideo(false);
      entityManager.persist(video1);

      Video video2 = createVideo(true);
      entityManager.persist(video2);

      List<Video> actual = videoRepository.findAllByIsDeleted(false);
      assertThat(actual).hasSize(1);
      assertThat(actual.get(0).isDeleted()).isFalse();
    }
  }
}
