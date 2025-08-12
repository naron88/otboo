package com.otbooalone.module.auth.dto.security;

import com.otbooalone.module.auth.dto.data.AuthUserDto;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails, CustomUserPrincipal {

  private final AuthUserDto userDto;
  private final String password;

  public static CustomUserDetails create(AuthUserDto authUserDto) {
    return new CustomUserDetails(authUserDto, null);
  }

  public static CustomUserDetails createWithPassword(AuthUserDto authUserDto, String password) {
    return new CustomUserDetails(authUserDto, password);
  }

  private CustomUserDetails(AuthUserDto userDto, String password) {
    this.userDto = userDto;
    this.password = password;
  }

  @Override
  public UUID getId() {
    return this.userDto.userId();
  }

  @Override
  public AuthUserDto getAuthUserDto() {
    return this.userDto;
  }

  // 사용자의 권한 정보
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.userDto.role()));
  }

  // 사용자의 비밀번호
  @Override
  public String getPassword() {
    return this.password;
  }

  // 사용자의 아이디
  @Override
  public String getUsername() {
    return this.userDto.email();
  }

  // 사용자의 계정 잠금 상태를 반환 (false: 잠금 상태)
  @Override
  public boolean isAccountNonLocked() {
    return !userDto.locked();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomUserDetails that)) {
      return false;
    }
    return userDto.email().equals(that.userDto.email());
  }

  @Override
  public int hashCode() {
    return Objects.hash(userDto.email());
  }
}