package com.otbooalone.module.user.mapper;

import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDto toDto(User user, List<String> linkedOAuthProviders) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getEmail(),
        user.getName(),
        user.getRole(),
        linkedOAuthProviders,
        user.isLocked());
  }
}
