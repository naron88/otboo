package com.otbooalone.module.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otbooalone.global.exception.ErrorResponse;
import com.otbooalone.global.util.IpUtils;
import com.otbooalone.module.auth.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 인증 실패 시 동작 (401)
 * jwt가 없거나, 잘못된 토큰, 인증 안된 사용자 등등
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException authException) throws IOException, ServletException {

    log.info("인증 실패 (이유: {} - {}, IP: {}, Method: {})",
      authException.getClass().getSimpleName(),
      authException.getMessage(),
      IpUtils.getClientIp(request),
      request.getRequestURI()
    );

    AuthErrorCode errorCode = AuthErrorCode.AUTHENTICATION_REQUIRED;

    ErrorResponse errorResponse = ErrorResponse.of(
      AuthenticationException.class.getSimpleName(),
      errorCode.getMessage()
    );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
