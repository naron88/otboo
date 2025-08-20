package com.otbooalone.module.user.mapper;

import com.otbooalone.module.location.dto.data.LocationDto;
import com.otbooalone.module.user.dto.data.ProfileDto;
import com.otbooalone.module.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

  public ProfileDto toDto(User user, LocationDto locationDto) {
    return new ProfileDto(
        user.getId(),
        user.getName(),
        user.getGender(),
        user.getBirthDate(),
        locationDto,
        user.getTemperatureSensitivity(),
        user.getProfileImageUrl()
    );
  }
}
