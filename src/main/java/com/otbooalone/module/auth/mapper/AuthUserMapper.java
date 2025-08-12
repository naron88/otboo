package com.otbooalone.module.auth.mapper;

import com.otbooalone.module.auth.dto.data.AuthUserDto;
import com.otbooalone.module.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthUserMapper {

  public AuthUserDto toDto(User user) {
    return new AuthUserDto(
        user.getId(),
        user.getEmail(),
        user.getName(),
        user.isLocked(),
        user.getRole()
    );
  }
}
