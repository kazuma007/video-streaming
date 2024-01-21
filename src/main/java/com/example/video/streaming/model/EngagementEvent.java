package com.example.video.streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Table(name = "engagement_events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngagementEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id")
  private long eventId;

  @Column(
      name = "event_type",
      nullable = false,
      columnDefinition = "VARCHAR CHECK (event_type IN ('impression', 'view'))")
  private String eventType;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private Timestamp createdAt;

  @ManyToOne
  @JoinColumn(name = "video_id")
  private Video video;
}
