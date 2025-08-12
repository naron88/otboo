package com.otbooalone.module.auth.entity;

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
@Table(name = "auth_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class AuthToken{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @CreatedDate
  @Column(columnDefinition = "timestamptz", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false, unique = true)
  UUID userId;

  @Column(columnDefinition = "TEXT")
  String accessToken;

  @Column(columnDefinition = "TEXT")
  String refreshToken;

  public static AuthToken create(UUID userId, String accessToken, String refreshToken) {
    return new AuthToken(userId, accessToken, refreshToken);
  }

  private AuthToken(UUID userId, String accessToken, String refreshToken) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public void replaceToken(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
