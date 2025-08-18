package com.otbooalone.module.user.controller;

import com.otbooalone.global.enums.SortDirection;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.dto.data.UserDtoCursorResponse;
import com.otbooalone.module.user.dto.request.UserCreateRequest;
import com.otbooalone.module.user.dto.request.UserLockUpdateRequest;
import com.otbooalone.module.user.dto.request.UserRoleUpdateRequest;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDtoCursorResponse> findByCursor(
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) UUID idAfter,
      @RequestParam int limit,
      @RequestParam String sortBy,
      @RequestParam SortDirection sortDirection,
      @RequestParam(required = false) String emailLike,
      @RequestParam(required = false) Role roleEqual,
      @RequestParam(required = false) Boolean locked
  ) {
    log.info("사용자 조회 요청: cursor = {}, idAfter = {}, limit = {}, sortBy = {}, sortDirection = {}, "
            + "emailLike = {}, roleEqual = {}, locked = {}", cursor, idAfter, limit, sortBy,
        sortDirection, emailLike, roleEqual, locked);

    UserDtoCursorResponse response = userService.findByCursor(cursor, idAfter, limit, sortBy,
        sortDirection, emailLike, roleEqual, locked);

    log.info(
        "사용자 조회 응답: dataSize = {}, nextCursor = {}, nextIdAfter = {}, hasNext = {}, totalCount = {}, "
            + "sortBy = {}, sortDirection = {}", response.data().size(), response.nextCursor(),
        response.nextIdAfter(), response.hasNext(), response.totalCount(), response.sortBy(),
        response.sortDirection());

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 사용자 권한 수정
  @PatchMapping("/{userId}/role")
  public ResponseEntity<UserDto> updateRole(
      @PathVariable UUID userId,
      @RequestBody UserRoleUpdateRequest request
  ) {
    log.info("사용자 권한 수정 요청: role = {}", request.role());

    UserDto response = userService.updateRole(userId, request.role());

    log.info("사용자 권한 수정 응답: role = {}", response.role());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 사용자 잠금 수정
  @PatchMapping("/{userId}/lock")
  public ResponseEntity<UserDto> updateLocked(
      @PathVariable UUID userId,
      @RequestBody UserLockUpdateRequest request
  ) {
    log.info("사용자 잠금 수정 요청: locked = {}", request.locked());

    UserDto response = userService.updateLocked(userId, request.locked());

    log.info("사용자 잠금 수정 응답: locked = {}", response.locked());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
