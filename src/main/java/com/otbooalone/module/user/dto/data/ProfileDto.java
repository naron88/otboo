package com.otbooalone.module.user.dto.data;

import com.otbooalone.module.location.dto.data.LocationDto;
import com.otbooalone.module.user.entity.User.Gender;
import java.time.LocalDate;
import java.util.UUID;

public record ProfileDto(
  UUID userId,
  String name,
  Gender gender,
  LocalDate birthDate,
  LocationDto location,
  int temperatureSensitivity,
  String profileImageUrl
) {

}
