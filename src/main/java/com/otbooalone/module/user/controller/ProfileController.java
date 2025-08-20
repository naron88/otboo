package com.otbooalone.module.user.controller;

import com.otbooalone.module.user.dto.data.ProfileDto;
import com.otbooalone.module.user.dto.request.ProfileUpdateRequest;
import com.otbooalone.module.user.service.ProfileService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping("/{userId}/profiles")
  public ResponseEntity<ProfileDto> findByUserId(@PathVariable UUID userId) {
    log.info("프로필 조회 요청");

    ProfileDto response = profileService.findByUserId(userId);

    log.info("프로필 조회 응답");

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PatchMapping("/{userId}/profiles")
  public ResponseEntity<ProfileDto> updateProfile(
      @PathVariable UUID userId,
      @Valid @RequestPart(name = "request") ProfileUpdateRequest request,
      @RequestPart(name = "image", required = false) MultipartFile image
  ) {
    log.info("프로필 수정 요청");
    ProfileDto response = profileService.update(userId, request, image);
    log.info("프로필 수정 응답");
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
