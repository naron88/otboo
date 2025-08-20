package com.otbooalone.module.user.service;

import com.otbooalone.module.location.dto.data.LocationDto;
import com.otbooalone.module.location.service.LocationService;
import com.otbooalone.module.storage.FileStorage;
import com.otbooalone.module.storage.StorageDomain;
import com.otbooalone.module.storage.exception.FileUploadFailedException;
import com.otbooalone.module.user.dto.data.ProfileDto;
import com.otbooalone.module.user.dto.request.ProfileUpdateRequest;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.exception.UserNotFoundException;
import com.otbooalone.module.user.mapper.ProfileMapper;
import com.otbooalone.module.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final LocationService locationService;
  private final ProfileMapper profileMapper;
  private final FileStorage fileStorage;

  @Transactional(readOnly = true)
  public ProfileDto findByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    LocationDto locationDto = user.getLocationId() == null ? null : locationService.findById(user.getLocationId());

    return profileMapper.toDto(user, locationDto);
  }

  public ProfileDto update(UUID userId, ProfileUpdateRequest request, MultipartFile image) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    if (image != null && !image.isEmpty()) {

      // 이미 이미지가 있었으면 삭제
      if (user.getProfileImageUrl() != null) {
        removeClothesImage(user.getProfileImageUrl());
      }

      // 새로운 이미지 업로드 및 엔티티 업데이트
      String newUrl = uploadClothesImage(image);
      user.updateImageUrl(newUrl);
    }

    if (request.name() != null && !request.name().equals(user.getName())) {
      user.updateName(request.name());
    }

    if (request.gender() != null && !request.gender().equals(user.getGender())) {
      user.updateGender(request.gender());
    }

    if (request.birthDate() != null && !request.birthDate().equals(user.getBirthDate())) {
      user.updateBirthDate(request.birthDate());
    }

    if (request.temperatureSensitivity() != null && !request.temperatureSensitivity().equals(user.getTemperatureSensitivity())) {
      user.updateTemperatureSensitivity(request.temperatureSensitivity());
    }

    LocationDto locationDto = null;
    if (request.location() != null) {
      locationDto = locationService.findById(user.getLocationId());

      if (locationDto.latitude() != request.location().latitude() || locationDto.longitude() != request.location().longitude()) {
        locationDto = locationService.create(request.location().latitude(), request.location().longitude());
        user.updateLocationId(locationDto.id());
      }
    }

    return profileMapper.toDto(user, locationDto);
  }

  // 이미지 업로드
  private String uploadClothesImage(MultipartFile image) {

    log.debug("이미지 업로드 시작");

    if (image == null || image.isEmpty()) {
      log.debug("이미지가 없습니다. return = null");
      return null;
    }

    try {
      String url = fileStorage.create(image, StorageDomain.PROFILE);

      log.debug("이미지 업로드 완료: url = {}", url);
      return url;
    } catch (FileUploadFailedException e) {

      log.warn("message = {}, details = {}", e.getMessage(), e.getDetails());
      return null;
    }
  }

  // 이미지 삭제
  private void removeClothesImage(String url) {
    log.debug("이미지 삭제 시작");

    if (url == null) {
      log.debug("이미지가 없습니다.");
    } else {

      try {
        fileStorage.delete(url);

        log.debug("이미지 삭제 완료: url = {}", url);
      } catch (FileUploadFailedException e) {

        log.warn("message = {}, details = {}", e.getMessage(), e.getDetails());
      }
    }
  }
}
