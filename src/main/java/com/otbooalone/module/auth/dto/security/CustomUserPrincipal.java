package com.otbooalone.module.auth.dto.security;

import com.otbooalone.module.auth.dto.data.AuthUserDto;
import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;

public interface CustomUserPrincipal {

  UUID getId();

  AuthUserDto getAuthUserDto();

  Collection<? extends GrantedAuthority> getAuthorities();
}
