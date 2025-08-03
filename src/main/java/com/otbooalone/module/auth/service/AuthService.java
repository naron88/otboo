package com.otbooalone.module.auth.service;

import com.otbooalone.module.auth.dto.request.UserCreateRequest;
import com.otbooalone.module.user.UserRepository;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserDto create(UserCreateRequest request, Role role) {
    log.debug("회원 가입 시작: name = {}, email = {}, role = {}", request.name(), request.email(), role);

    User user = User.createUser(request.email(), request.name(), passwordEncoder.encode(
        request.password()), role);
    User savedUser = userRepository.save(user);

    return userMapper.toDto(savedUser, List.of());
  }
}
