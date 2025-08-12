package com.otbooalone.module.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.otbooalone.module.auth.dto.data.AuthUserDto;
import com.otbooalone.module.auth.dto.data.TempPasswordMetadata;
import com.otbooalone.module.auth.entity.AuthToken;
import com.otbooalone.module.auth.entity.GeneratedToken;
import com.otbooalone.module.auth.exception.common.AccountLockedException;
import com.otbooalone.module.auth.exception.common.AuthenticationFailedException;
import com.otbooalone.module.auth.exception.common.InvalidTokenException;
import com.otbooalone.module.auth.exception.jwt.InvalidJwtSignatureException;
import com.otbooalone.module.auth.exception.jwt.JwtExpiredException;
import com.otbooalone.module.auth.mapper.AuthUserMapper;
import com.otbooalone.module.auth.provider.JwtTokenProvider;
import com.otbooalone.module.auth.repository.AuthTokenRepository;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthTokenRepository authTokenRepository;

  @Spy
  private AuthUserMapper authUserMapper;

  @InjectMocks
  private AuthService authService;

  String refreshToken = "유효한-refresh-token";
  String accessToken = "유효한-access-token";
  User user = User.createUser("test1@test.com", "test", "password!", Role.USER);
  UUID userId = UUID.randomUUID();
  AuthToken authToken = AuthToken.create(userId, accessToken, refreshToken);

  @BeforeEach
  void setUp() {

    ReflectionTestUtils.setField(user, "id", userId);

  }
  @DisplayName("토큰 재발급 성공")
  @Test
  void refreshTokens_success() {
    AuthUserDto authUserDto = authUserMapper.toDto(user);
    TempPasswordMetadata tempPasswordMetadata = TempPasswordMetadata.notUsed();

    // given
    when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
    doNothing().when(jwtTokenProvider).validateToken(refreshToken);
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(authTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken))
      .thenReturn(Optional.of(authToken));
    when(jwtTokenProvider.getTempPasswordMetaDataFromToken(refreshToken))
      .thenReturn(tempPasswordMetadata);
    when(authUserMapper.toDto(user)).thenReturn(authUserDto);
    when(jwtTokenProvider.generateToken(authUserDto, tempPasswordMetadata))
      .thenReturn(new GeneratedToken("new-access-token", "new-refresh-token"));

    // when
    GeneratedToken result = authService.refreshTokens(refreshToken);

    // then
    assertThat(result).isNotNull();
    assertThat(result.accessToken()).isEqualTo("new-access-token");
    assertThat(result.refreshToken()).isEqualTo("new-refresh-token");
  }

  @DisplayName("액세스 토큰 조회")
  @Nested
  class GetAccessTokenByRefreshToken {

    @DisplayName("정상적인 refreshToken이 주어지면 accessToken을 반환한다")
    @Test
    void getAccessToken_success() {
      // given
      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doNothing().when(jwtTokenProvider).validateToken(refreshToken);
      when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
      when(authTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken))
        .thenReturn(Optional.of(authToken));
      doNothing().when(jwtTokenProvider).validateToken(authToken.getAccessToken());

      // when
      String result = authService.getAccessTokenByRefreshToken(refreshToken);

      // then
      assertThat(result).isEqualTo(authToken.getAccessToken());
    }

    @DisplayName("유효하지 않은 리프레시 토큰이면 에외가 발생한다")
    @Test
    void getAccessToken_shouldThrowException_IfSignatureIsInvalid() {
      // given
      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doThrow(new InvalidJwtSignatureException("서명 실패")).when(jwtTokenProvider)
        .validateToken(refreshToken);

      // when & then
      assertThrows(InvalidTokenException.class,
        () -> authService.getAccessTokenByRefreshToken(refreshToken));
    }

    @DisplayName("토큰에 해당하는 유저가 존재하지 않으면 예외가 발생한다")
    @Test
    void getAccessToken_shouldThrowException_IfUserDoesNotExist() {
      // given
      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doNothing().when(jwtTokenProvider).validateToken(refreshToken);
      when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

      // when & then
      assertThrows(AuthenticationFailedException.class,
        () -> authService.getAccessTokenByRefreshToken(refreshToken));
    }

    @DisplayName("계정이 잠금 상태이면 예외가 발생한다")
    @Test
    void getAccessToken_shouldThrowException_IfUserIsLocked() {
      // given
      User lockedUser = User.createUser("test@test.com", "name", "pw", Role.USER);
      lockedUser.lock();

      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doNothing().when(jwtTokenProvider).validateToken(refreshToken);
      when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(lockedUser));

      // when & then
      assertThrows(AccountLockedException.class,
        () -> authService.getAccessTokenByRefreshToken(refreshToken));
    }

    @DisplayName("refreshToken으로 accessToken이 조회되지 않으면 예외가 발생한다")
    @Test
    void getAccessToken_shouldThrowException_IfAccessTokenNotFound() {
      // given
      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doNothing().when(jwtTokenProvider).validateToken(refreshToken);
      when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
      when(authTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken))
        .thenReturn(Optional.empty());

      // when & then
      assertThrows(InvalidTokenException.class,
        () -> authService.getAccessTokenByRefreshToken(refreshToken));
    }

    @DisplayName("저장된 accessToken이 유효하지 않으면 예외가 발생한다")
    @Test
    void getAccessToken_shouldThrowException_IfAccessTokenIsInvalid() {
      // given
      when(jwtTokenProvider.getSubjectFromToken(refreshToken)).thenReturn(user.getEmail());
      doNothing().when(jwtTokenProvider).validateToken(refreshToken);
      when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
      when(authTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken))
        .thenReturn(Optional.of(authToken));
      doThrow(new JwtExpiredException("만료됨")).when(jwtTokenProvider)
        .validateToken(authToken.getAccessToken());

      // when & then
      assertThrows(InvalidTokenException.class,
        () -> authService.getAccessTokenByRefreshToken(refreshToken));
    }
  }

  @DisplayName("강제 로그아웃 성공")
  @Test
  void forceLogout_success() {
    // given
    UUID userId = UUID.randomUUID();

    // when
    authService.forceLogout(userId);

    // then
    verify(authTokenRepository).deleteByUserId(userId);
  }
}