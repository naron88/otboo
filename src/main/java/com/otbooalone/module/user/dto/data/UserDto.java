package com.otbooalone.module.user.dto.data;

import com.otbooalone.module.user.entity.User.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDto(
    UUID id,
    LocalDateTime createdAt,
    String email,
    String name,
    Role role,
    List<String> linkedOAuthProviders,
    boolean locked
) {

}

