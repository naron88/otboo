package com.otbooalone.module.auth.exception.jwt;

public class AccessTokenReplacedException extends JwtAuthenticationException {

  public AccessTokenReplacedException(String msg) {
    super(msg);
  }
}
