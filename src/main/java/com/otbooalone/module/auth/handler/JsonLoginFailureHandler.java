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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonLoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    AuthErrorCode errorCode;

    // 로그인 실패 예외 확인
    if (exception instanceof BadCredentialsException) {
      errorCode = AuthErrorCode.INVALID_CREDENTIALS;
    } else if (exception instanceof CredentialsExpiredException) {
      errorCode = AuthErrorCode.CREDENTIALS_EXPIRED;
    } else if (exception instanceof LockedException) {
      errorCode = AuthErrorCode.ACCOUNT_LOCKED;
    } else {
      errorCode = AuthErrorCode.AUTHENTICATION_FAILED;
    }

    log.info("로그인 실패 (이유: {}, IP: {})", exception.getClass().getSimpleName(),
        IpUtils.getClientIp(request));

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
