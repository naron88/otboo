package com.otbooalone.module.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @CreatedDate
  @Column(columnDefinition = "timestamptz", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(columnDefinition = "timestamptz")
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private LocalDate birthDate;

  private int temperatureSensitivity;

  @Column(columnDefinition = "text")
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(nullable = false)
  private boolean locked;

  private UUID locationId;

  private User(String email, String name, String password, Role role) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.role = role;
    this.locked = false;
  }

  public static User createUser(String email, String name, String password, Role role) {
    return new User(email, name, password, role);
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateGender(Gender gender) {
    this.gender = gender;
  }

  public void updateBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public void updateTemperatureSensitivity(int temperatureSensitivity) {
    this.temperatureSensitivity = temperatureSensitivity;
  }

  public void updateLocationId(UUID locationId) {
    this.locationId = locationId;
  }

  public void updateImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void lock() {
    locked = true;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public void updateLocked(boolean locked) {
    this.locked = locked;
  }

  public enum Gender {
    MALE, FEMALE, OTHER
  }

  public enum Role {
    USER, ADMIN
  }
}
