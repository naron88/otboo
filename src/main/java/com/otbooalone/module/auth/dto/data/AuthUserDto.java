package com.otbooalone.module.auth.dto.data;

import com.otbooalone.module.user.entity.User.Role;
import java.util.UUID;

public record AuthUserDto(
    UUID userId,
    String email,
    String name,
    boolean locked,
    Role role
) {

}
