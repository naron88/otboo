package com.otbooalone.module.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otbooalone.global.util.CookieUtil;
import com.otbooalone.module.auth.dto.data.AuthUserDto;
import com.otbooalone.module.auth.dto.data.TempPasswordMetadata;
import com.otbooalone.module.auth.dto.security.CustomUserDetails;
import com.otbooalone.module.auth.entity.GeneratedToken;
import com.otbooalone.module.auth.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // 인증 정보
    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    AuthUserDto authUserDto = principal.getAuthUserDto();

    // 토큰 발급
    TempPasswordMetadata tempPasswordMeta = checkTempPasswordMetadata(authentication);
    GeneratedToken generatedToken = jwtTokenProvider.generateToken(authUserDto, tempPasswordMeta);

    // 쿠키 생성 (refresh token)
    Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(generatedToken.refreshToken());
    response.addCookie(refreshTokenCookie);

    // 응답 설정
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(HttpServletResponse.SC_OK);

    // 액세스 토큰 응답
    objectMapper.writeValue(response.getWriter(), generatedToken.accessToken());
  }

  // Authentication 객체에서 임시 비빌번호 인증인지 확인
  private TempPasswordMetadata checkTempPasswordMetadata(Authentication authentication) {
    Object detailsObj = authentication.getDetails();
    if (detailsObj instanceof Map<?, ?> details) {
      Object tempPasswordMeta = details.get("tempPassword");
      if (tempPasswordMeta instanceof TempPasswordMetadata) {
        return (TempPasswordMetadata) tempPasswordMeta;
      }
    }
    // details가 없거나 타입이 다르면 기본값 리턴하거나 null 반환
    return TempPasswordMetadata.notUsed(); // 임시 비밀번호 미사용 기본값 예시
  }
}
