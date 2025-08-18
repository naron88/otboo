package com.otbooalone.module.user.service;

import com.otbooalone.global.enums.SortDirection;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.dto.data.UserDtoCursorResponse;
import com.otbooalone.module.user.dto.request.UserCreateRequest;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.exception.EmailAlreadyExistsException;
import com.otbooalone.module.user.exception.UserNotFoundException;
import com.otbooalone.module.user.mapper.UserCursorResponseMapper;
import com.otbooalone.module.user.mapper.UserMapper;
import com.otbooalone.module.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;
  private final UserCursorResponseMapper userCursorResponseMapper;

  public UserDto create(UserCreateRequest request, Role role) {
    log.debug("회원 가입 시작: name = {}, email = {}, role = {}", request.name(), request.email(), role);

    if (userRepository.existsByEmail(request.email())) {
      throw EmailAlreadyExistsException.withEmail(request.email());
    }

    User user = User.createUser(request.email(), request.name(), passwordEncoder.encode(
        request.password()), role);
    User savedUser = userRepository.save(user);

    return userMapper.toDto(savedUser, List.of());
  }

  public UserDtoCursorResponse findByCursor(String cursor, UUID idAfter, int limit, String sortBy,
      SortDirection sortDirection, String emailLike, Role roleEqual, Boolean locked) {

    // 이메일 키워드로 찾기
    List<User> users = emailLike == null
        ? userRepository.findAll()
        : userRepository.findByKeyword(emailLike);

    if (roleEqual != null) {
      users = users.stream()
          .filter(user -> user.getRole() == roleEqual)
          .toList();
    }

    if (locked != null) {
      users = users.stream()
          .filter(user -> user.isLocked() == locked)
          .toList();
    }

    List<User> usersByCursor = userRepository.findByCursor(users.stream().map(User::getId).toList(),
        cursor, idAfter, limit, sortBy, sortDirection);

    boolean hasNext = usersByCursor.size() > limit;
    String nextCursor = null;
    UUID nextIdAfter = null;

    if (hasNext) {
      usersByCursor = usersByCursor.subList(0, limit);
      User lastUser = usersByCursor.get(usersByCursor.size() - 1);
      nextCursor = lastUser.getName();
      nextIdAfter = lastUser.getId();
    }

    int totalCount = users.size();

    List<UserDto> userDtos = usersByCursor.stream()
        .map(user -> userMapper.toDto(user, List.of()))
        .toList();

    return userCursorResponseMapper.toDto(userDtos, nextCursor, nextIdAfter, hasNext, totalCount,
        sortBy, sortDirection);
  }

  public UserDto updateRole(UUID userId, Role role) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    user.updateRole(role);

    return userMapper.toDto(user, List.of());
  }

  public UserDto updateLocked(UUID userId, boolean locked) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    user.updateLocked(locked);

    return userMapper.toDto(user, List.of());
  }
}
