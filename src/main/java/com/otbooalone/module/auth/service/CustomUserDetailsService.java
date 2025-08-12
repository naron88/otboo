package com.otbooalone.module.auth.service;

import com.otbooalone.module.auth.dto.security.CustomUserDetails;
import com.otbooalone.module.auth.mapper.AuthUserMapper;
import com.otbooalone.module.user.exception.UserNotFoundException;
import com.otbooalone.module.user.repository.UserRepository;
import com.otbooalone.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  public final UserRepository userRepository;
  public final AuthUserMapper authUserMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    return CustomUserDetails.createWithPassword(
        authUserMapper.toDto(user),
        user.getPassword());
  }
}
