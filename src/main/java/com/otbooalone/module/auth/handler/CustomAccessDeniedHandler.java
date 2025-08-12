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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// 인가 실패 시 동작 (403)
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
    AccessDeniedException accessDeniedException) throws IOException, ServletException {

    log.info("접근 권한 없음 (이유: {} - {}, IP: {})",
      accessDeniedException.getClass().getSimpleName(),
      accessDeniedException.getMessage(),
      IpUtils.getClientIp(request));

    AuthErrorCode errorCode = AuthErrorCode.ACCESS_DENIED;

    ErrorResponse errorResponse = ErrorResponse.of(
      accessDeniedException.getClass().getSimpleName(),
      accessDeniedException.getMessage()
    );

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
