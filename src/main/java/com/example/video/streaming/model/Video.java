package com.example.video.streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "videos")
@Data
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "video_id")
  private long videoId;

  @Column(name = "content_link")
  private String contentLink;

  @Column private String title;

  @Column(columnDefinition = "TEXT")
  private String synopsis;

  @Column private String director;

  @Column private String actor;

  @Column(name = "year_of_release")
  private Short yearOfRelease;

  @Column private String genre;

  @Column(name = "running_time")
  private Short runningTime;

  @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean isDeleted;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "video")
  @Builder.Default
  private List<EngagementEvent> engagementEvents = Collections.emptyList();
}
