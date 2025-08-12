package com.otbooalone.module.auth.exception.jwt;

public class AccessTokenNotFoundException extends JwtAuthenticationException {

  public AccessTokenNotFoundException(String msg) {
    super(msg);
  }
}
