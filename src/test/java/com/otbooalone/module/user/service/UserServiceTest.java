package com.otbooalone.module.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.otbooalone.module.auth.dto.request.UserCreateRequest;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.mapper.UserMapper;
import com.otbooalone.module.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Spy
  private PasswordEncoder passwordEncoder;

  @Spy
  private UserMapper userMapper;

  @Nested
  @DisplayName("회원 가입")
  class Create {

    @Test
    @DisplayName("회원 가입 성공")
    void create_success() {

      // given
      UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "qwer1234!");
      Role role = Role.USER;
      User user = User.createUser(request.email(), request.name(), passwordEncoder.encode(
          request.password()), role);

      given(userRepository.save(any(User.class))).willReturn(user);

      UserDto userDto = userMapper.toDto(user, List.of());

      // when
      UserDto result = userService.create(request, role);

      // then
      assertNotNull(result);
      assertEquals(request.email(), userDto.email());
      assertEquals(request.name(), userDto.name());

      then(userRepository).should().save(any(User.class));
    }
  }
}