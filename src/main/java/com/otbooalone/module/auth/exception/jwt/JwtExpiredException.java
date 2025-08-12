package com.otbooalone.module.auth.exception.jwt;

/**
 * 토큰 만료 예외
 */
public class JwtExpiredException extends JwtAuthenticationException {

  public JwtExpiredException(String msg) {
    super(msg);
  }
}
