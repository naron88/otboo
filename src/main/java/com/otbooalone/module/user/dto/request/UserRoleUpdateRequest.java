package com.otbooalone.module.user.dto.request;

import com.otbooalone.module.user.entity.User.Role;

public record UserRoleUpdateRequest(
    Role role
) {

}
