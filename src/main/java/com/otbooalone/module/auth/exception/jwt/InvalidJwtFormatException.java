package com.otbooalone.module.auth.exception.jwt;

public class InvalidJwtFormatException extends JwtAuthenticationException {

  public InvalidJwtFormatException(String msg) {
    super(msg);
  }
}
