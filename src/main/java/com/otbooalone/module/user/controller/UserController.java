package com.otbooalone.module.user.controller;

import com.otbooalone.module.auth.dto.request.UserCreateRequest;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping
  public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateRequest request) {
    log.info("회원 가입 요청: name = {}, email = {}", request.name(), request.email());

    UserDto response = userService.create(request, Role.USER);

    log.info("회원 가입 응답: name = {}, email = {}", response.name(), response.email());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
