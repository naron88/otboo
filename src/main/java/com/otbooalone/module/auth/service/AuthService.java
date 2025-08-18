package com.otbooalone.module.auth.service;

import com.otbooalone.module.auth.dto.data.AuthUserDto;
import com.otbooalone.module.auth.dto.data.TempPasswordMetadata;
import com.otbooalone.module.auth.entity.AuthToken;
import com.otbooalone.module.auth.entity.GeneratedToken;
import com.otbooalone.module.auth.exception.common.AccountLockedException;
import com.otbooalone.module.auth.exception.common.AuthenticationFailedException;
import com.otbooalone.module.auth.exception.common.InvalidTokenException;
import com.otbooalone.module.auth.mapper.AuthUserMapper;
import com.otbooalone.module.auth.provider.JwtTokenProvider;
import com.otbooalone.module.auth.repository.AuthTokenRepository;
import com.otbooalone.module.user.repository.UserRepository;
import com.otbooalone.module.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  private final AuthUserMapper authUserMapper;

  private final JwtTokenProvider jwtTokenProvider;
  private final AuthTokenRepository authTokenRepository;


  public String getAccessTokenByRefreshToken(String refreshToken) {

    // refreshToken 검증
    String userEmail = jwtTokenProvider.getSubjectFromToken(refreshToken);
    validateRefreshTokenOrThrow(userEmail, refreshToken);

    // 유저 확인
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(AuthenticationFailedException::new);

    // 잠금 유저 확인
    checkUserNotLockedOrThrow(user);

    // accessToken 가져오기
    AuthToken authToken = findAuthTokenByUserIdAndRefreshTokenOrThrow(user.getId(), refreshToken);

    // accessToken 유효성 검사
    String accessToken = authToken.getAccessToken();
    try {
      jwtTokenProvider.validateToken(accessToken);
    } catch (AuthenticationException ex) {
      log.info("저장된 accessToken 유효성 검사 실패 (userId: {})", user.getId());
      throw InvalidTokenException.noDetail();
    }
    return accessToken;
  }

  public GeneratedToken refreshTokens(String refreshToken) {
    // 유효성 검사
    String userEmail = jwtTokenProvider.getSubjectFromToken(refreshToken);
    validateRefreshTokenOrThrow(userEmail, refreshToken);

    // 유저 확인
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(AuthenticationFailedException::new);

    // 잠금 확인
    checkUserNotLockedOrThrow(user);

    // token 정보 확인
    findAuthTokenByUserIdAndRefreshTokenOrThrow(user.getId(), refreshToken);

    // 토큰에서 임시 비밀번호 정보 가져오기
    TempPasswordMetadata tempPassword = jwtTokenProvider.getTempPasswordMetaDataFromToken(
        refreshToken);

    AuthUserDto authUserDto = authUserMapper.toDto(user);

    return jwtTokenProvider.generateToken(authUserDto, tempPassword);
  }

  public void forceLogout(UUID userId) {
    authTokenRepository.deleteByUserId(userId);
    //sseService.disconnectAllEmitters(userId, "강제 로그아웃으로 연결 해제");
  }

  // 리프레시 토큰 검증
  private void validateRefreshTokenOrThrow(String userEmail, String refreshToken) {
    try {
      jwtTokenProvider.validateToken(refreshToken);
    } catch (AuthenticationException ex) {
      log.info("리프레시 토큰 검증 실패: {} (userEmail: {})", ex.getMessage(), userEmail);
      throw InvalidTokenException.noDetail();
    }
  }

  // 인증 토큰 가져오기
  private AuthToken findAuthTokenByUserIdAndRefreshTokenOrThrow(UUID userId, String refreshToken) {
    return authTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken)
        .orElseThrow(() -> {
          log.info("refreshToken에 해당하는 인증 토큰이 존재하지 않습니다. (userId: {})", userId);
          return InvalidTokenException.noDetail();
        });
  }

  private void checkUserNotLockedOrThrow(User user) {
    if (user.isLocked()) {
      log.info("잠금 계정이 액세스 토큰 조회 시도 (userId: {})", user.getId());
      throw AccountLockedException.noDetail();
    }
  }
}
