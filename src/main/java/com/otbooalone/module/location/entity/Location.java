package com.otbooalone.module.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "locations")
@EntityListeners(AuditingEntityListener.class)
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @CreatedDate
  @Column(columnDefinition = "timestamptz", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Column(nullable = false)
  private double x;

  @Column(nullable = false)
  private double y;

  @Column(nullable = false)
  private String name;

  public static Location create(double latitude, double longitude, double x, double y, String name) {
    return new Location(latitude, longitude, x, y, name);
  }

  private Location(double latitude, double longitude, double x, double y, String name) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.x = x;
    this.y = y;
    this.name = name;
  }
}
